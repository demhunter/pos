/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.area.AreaDto;
import com.pos.basic.service.AreaService;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.condition.orderby.ConsoleSessionOrderField;
import com.pos.im.condition.orderby.SessionOrderField;
import com.pos.im.condition.query.SessionClosedCondition;
import com.pos.im.condition.query.SessionCommonCondition;
import com.pos.im.condition.query.SessionConsoleCondition;
import com.pos.im.constant.InternalMessageType;
import com.pos.im.constant.MessageIdentifier;
import com.pos.im.constant.SessionClosedReason;
import com.pos.im.constant.UserJoinSessionType;
import com.pos.im.dao.*;
import com.pos.im.domain.*;
import com.pos.im.dto.session.*;
import com.pos.im.dto.message.InternalMessageDto;
import com.pos.im.dto.session.build.SessionBuildByCase;
import com.pos.im.dto.session.build.SessionBuildDto;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.exception.IMErrorCode;
import com.pos.im.exception.IMServerException;
import com.pos.im.service.IMMessageService;
import com.pos.im.service.IMSessionService;
import com.pos.im.service.IMStatisticsService;
import com.pos.im.service.IMUserService;
import com.pos.im.service.support.IMClient;
import com.pos.im.service.support.NoticeTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.pos.im.service.support.IMClient.GroupAction;

/**
 * IM会话服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/7/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IMSessionServiceImpl implements IMSessionService {

    private Logger logger = LoggerFactory.getLogger(IMSessionServiceImpl.class);

    @Resource
    private UserTokenDao userTokenDao;

    @Resource
    private IMClient imClient;

    @Resource
    private NoticeTemplate noticeTemplate;

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private CompanyInfoDao companyInfoDao;

    @Resource
    private SessionDao sessionDao;

    @Resource
    private SessionCaseDao sessionCaseDao;

    @Resource
    private SessionMemberDao sessionMemberDao;

    @Resource
    private SessionCompanyDao sessionCompanyDao;

    @Resource
    private IMStatisticsService imStatisticsService;

    @Resource
    private IMMessageService imMessageService;

    @Resource
    private SmsService smsService;

    @Resource
    private AreaService areaService;

    @Resource
    private SessionPlatformEmployeeDao sessionPlatformEmployeeDao;

    @Value("${im.sms.send.switch}")
    private String smsSendSwitch;

    @Value("${pe.session.count.limit.eighty}")
    private String peSessionCountLimitEighty;

    @Value("${pe.session.count.limit.hundred}")
    private String peSessionCountLimitHundred;

    @Override
    public ApiResult<SessionCreateReturnDto> create(SessionBuildDto buildDto) throws IMServerException {
        checkCreate(buildDto);

        // 创建会话并在持久化后返回对应实体
        Session session = saveSession(buildDto);
        saveSessionCompanies(buildDto, session);
        UserInfoDto platformEmployee = buildDto.getPlatformEmployee();
        List<SessionMember> members = saveSessionMembers(buildDto, session, platformEmployee);

        // 向IMServer申请创建群组
        String sessionIcon = formatSessionIcon(buildDto);
        String groupId = imClient.createGroup(session, sessionIcon, members);
        session.setGroupId(groupId);
        sessionDao.update(session);
        /*// 通知成员
        pushGroupCreatedNotice(session, buildDto);

        // 以家居顾问的名义自动发送SayHello
        for (SessionMember member : members) {
            if (platformEmployee != null
                    && member.getUserId().equals(platformEmployee.getUserId())
                    && member.getUserType().equals(platformEmployee.getUserType())) {
                String content = noticeTemplate.formatSessionDefaultGreeting(platformEmployee.getShowName())
                        + Strings.nullToEmpty(platformEmployee.getAppendGreeting());
                imClient.sendGroupTextMessage(session.getGroupId(), platformEmployee.buildUserIdentifier(), content);
                break;
            }
        }*/
        SessionCreateReturnDto result = new SessionCreateReturnDto();
        result.setSession(session);
        result.setMembers(members);
        result.setPlatformEmployee(platformEmployee);
        // return ApiResult.succ(new SessionCreatedDto(session.getId(), session.getGroupId(), session.getName()));
        return ApiResult.succ(result);
    }

    @Override
    public void sendSessionCreateMsg(SessionCreateReturnDto sessionReturn) {
        try {
            FieldChecker.checkEmpty(sessionReturn, "sessionReturn");
            sessionReturn.check("sessionReturn");
            Session session = sessionReturn.getSession();
            List<SessionMember> members = sessionReturn.getMembers();
            UserInfoDto platformEmployee = sessionReturn.getPlatformEmployee();

            // 通知成员
            pushGroupCreatedNotice(sessionReturn.getSession());

            // 以家居顾问的名义自动发送SayHello
            for (SessionMember member : members) {
                if (platformEmployee != null
                        && member.getUserId().equals(platformEmployee.getUserId())
                        && member.getUserType().equals(platformEmployee.getUserType())) {
                    String content = noticeTemplate.formatSessionDefaultGreeting(platformEmployee.getShowName())
                            + Strings.nullToEmpty(platformEmployee.getAppendGreeting());
                    imClient.sendGroupTextMessage(session.getGroupId(), platformEmployee.buildUserIdentifier(), content);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("发送会话创建成功通知消息失败！", e);
        }
    }

    @Override
    public ApiResult<Integer> addMembers(Long sessionId, Set<Long> membersId, String memberType,
                                         UserIdentifier operator) throws IMServerException {
        checkAddMembers(sessionId, membersId, memberType, operator);

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        List<SessionMember> existMembers = sessionMemberDao.findBySessionId(sessionId, null);
        // 检查会话成员上限(排除客服)
        if (existMembers.stream().filter(member ->
                member.isAvailable() && !member.isServant())
                .count() + membersId.size() > imClient.getSessionMaxMember()) {
            return ApiResult.fail(IMErrorCode.SESSION_MEMBER_LIMIT);
        }

        // 判断操作者是否已是会话成员
        UserInfo operatorInfo = userInfoDao.find(operator.getUserId(), operator.getUserType(), true);
        if (operatorInfo == null || existMembers.stream().filter(member ->
                member.isAvailable() && member.isSameUser(operatorInfo)).count() == 0) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }

        // 查询并检查要加入的成员列表
        List<UserInfo> members = userInfoDao.findUsers(
                membersId.stream().collect(Collectors.toList()), memberType, true);
        if (members == null || members.size() != membersId.size()) {
            throw new ValidationException("要添加的成员不存在！");
        }

        if (members.size() == 1){
            for (SessionMember sessionMember : existMembers){
                if (sessionMember.getUserId().equals(members.get(0).getUserId()) && sessionMember.isAvailable()){
                    return ApiResult.fail(IMErrorCode.USER_IN_JOIN_IM);
                }
            }
        }
        // 提取并过滤要加入的成员列表
        List<UserInfo> newMembers = new ArrayList<>();
        List<SessionMember> oldMembers = new ArrayList<>();
        begin:
        for (Iterator<UserInfo> it1 = members.iterator(); it1.hasNext(); ) {
            UserInfo member = it1.next();
            for (Iterator<SessionMember> it2 = existMembers.iterator(); it2.hasNext(); ) {
                SessionMember existMbr = it2.next();
                if (existMbr.isSameUser(member)) {
                    if (!existMbr.isAvailable()) {
                        oldMembers.add(existMbr);
                    } else {
                        it1.remove(); // 忽略已经加入的成员
                    }
                    it2.remove(); // 减少下次循环的比对次数
                    continue begin;
                }
            }
            newMembers.add(member);
        }
        if (members.isEmpty()) {
            return ApiResult.succ(Integer.valueOf(0));
        }

        // 检查成员会话上限
        checkMembersSessionLimit(members, memberType);
        // 新增/更新成员信息
        List<SessionMember> sessionMembers = addOrUpdateMembers(session, newMembers, oldMembers);
        // 向IMServer发送加群请求并向其他成员推送通知
        //imClient.createGroup(session, sessionMembers, GroupAction.JOIN);
        imClient.addGroupMembers(session, sessionMembers);
        pushAddMemberNotice(session, members, operatorInfo);

        return ApiResult.succ(Integer.valueOf(members.size()));
    }

    @Override
    public ApiResult<NullObject> removeMember(Long sessionId, UserIdentifier member,
                                              UserIdentifier operator) throws IMServerException {
        checkRemoveMembers(sessionId, member);

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        UserInfo memberInfo = userInfoDao.find(member.getUserId(), member.getUserType(), true);
        if (memberInfo == null) {
            throw new ValidationException("要添加的成员不存在！");
        }
        if (session.isCreator(member)) {
            throw new ValidationException("不能移除会话创建者！");
        }

        UserInfo operatorInfo = null;
        if (operator != null) {
            operator.check("operator");
            operatorInfo = userInfoDao.find(operator.getUserId(), operator.getUserType(), true);
            // 只有会话创建者/客服可以踢出成员
            if (operatorInfo == null || !(session.isCreator(
                    operatorInfo.getUserId(), operatorInfo.getUserType()) || operatorInfo.isManager())) {
                return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
            }
        }

        // 获取指定成员信息
        SessionMember sessionMember = sessionMemberDao.find(
                sessionId, memberInfo.getUserId(), memberInfo.getUserType());
        if (sessionMember == null || !sessionMember.isAvailable()) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }

        // 更新成员会话状态
        sessionMember.setAvailable(false);
        sessionMember.setUpdateTime(Calendar.getInstance().getTime());
        sessionMemberDao.update(sessionMember);

        // 向IMServer发送退群请求并向其他成员推送通知
        // imClient.quitGroup(session.getGroupId(), new UserIdentifier(sessionMember.getUserId(), sessionMember.getUserType()));
        imClient.removeGroupMember(session.getGroupId(),
                new UserIdentifier(session.getUserId(), session.getUserType()),
                new UserIdentifier(sessionMember.getUserId(), sessionMember.getUserType()));
        pushRemoveMemberNotice(session, memberInfo, operatorInfo);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> quitGroup(Long sessionId, UserIdentifier operator) throws IMServerException {
        checkRemoveMembers(sessionId, operator);

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        UserInfo operatorInfo = userInfoDao.find(operator.getUserId(), operator.getUserType(), true);
        if (operatorInfo == null) {
            throw new ValidationException("要移除的成员不存在！");
        }
        if (session.isCreator(operator)) {
            throw new ValidationException("会话创建者不能主动退出！");
        }

        // 获取指定成员信息
        SessionMember sessionMember = sessionMemberDao.find(
                sessionId, operator.getUserId(), operator.getUserType());
        if (sessionMember == null || !sessionMember.isAvailable()) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }

        // 更新成员会话状态
        sessionMember.setAvailable(false);
        sessionMember.setUpdateTime(Calendar.getInstance().getTime());
        sessionMemberDao.update(sessionMember);

        // 向IMServer发送退群请求并向其他成员推送通知
        imClient.quitGroup(session.getGroupId(), new UserIdentifier(sessionMember.getUserId(), sessionMember.getUserType()));
        pushRemoveMemberNotice(session, operatorInfo, operatorInfo);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> addServant(Long sessionId, Long servantId) throws IMServerException {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(servantId, "userId");

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        // 判断是否客服身份
        UserInfo servant = userInfoDao.find(servantId, IMUserService.USER_TYPE_MANAGER, true);
        if (servant == null) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        }

        SessionMember existEntity = null;
        List<SessionMember> existMembers = sessionMemberDao.findBySessionId(sessionId, null);
        for (SessionMember member : existMembers) {
            if (member.isSameUser(servant)) {
                // 已加入会话不能重复提交
                if (member.isAvailable()) {
                    return ApiResult.fail(IMErrorCode.MEMBER_IS_JOINED);
                }
                existEntity = member;
            } else if (member.isServant()) {
                // 每个会话只能有一个客服
                if (member.isAvailable()) {
                    return ApiResult.fail(IMErrorCode.SESSION_HAS_SERVANT);
                }
            }
        }

        // 检查客服会话上限
        checkOperatorSessionLimit(servant.getUserId(), servant.getUserType());

        // 新增/更新成员信息
        List<SessionMember> sessionMembers = new ArrayList<>(1);
        if (existEntity != null) {
            existEntity.setAvailable(true);
            existEntity.setUpdateTime(Calendar.getInstance().getTime());
            sessionMemberDao.update(existEntity);
            sessionMembers.add(existEntity);
        } else {
            SessionMember newEntity = createSessionMemberEntity(
                    session.getId(), servant.getUserId(), servant.getUserType(),
                    null, UserJoinSessionType.SERVANT, Calendar.getInstance().getTime());
            sessionMemberDao.save(newEntity);
            sessionMembers.add(newEntity);
        }

        // 向IMServer发送加群请求并向其他成员推送通知
        // imClient.createGroup(session, sessionMembers, GroupAction.JOIN);
        imClient.addGroupMembers(session, sessionMembers);
        pushMemberActionNotice(session, servant, GroupAction.JOIN);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> removeServant(Long sessionId, Long servantId) throws IMServerException {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(servantId, "userId");

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        // 判断是否客服身份
        UserInfo servant = userInfoDao.find(servantId, IMUserService.USER_TYPE_MANAGER, true);
        if (servant == null) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        }

        // 获取指定成员信息
        SessionMember member = sessionMemberDao.find(
                sessionId, servant.getUserId(), servant.getUserType());
        if (member == null || !member.isAvailable()) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }

        // 更新成员会话状态
        member.setAvailable(false);
        member.setUpdateTime(Calendar.getInstance().getTime());
        sessionMemberDao.update(member);

        // 向IMServer发送退群请求并向其他成员推送通知
        imClient.quitGroup(session.getGroupId(), new UserIdentifier(member.getUserId(), member.getUserType()));
        pushMemberActionNotice(session, servant, GroupAction.QUIT);

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> close(Long sessionId, UserIdentifier operator) throws IMServerException {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(operator, "operator");
        operator.check("operator");

        // 只有业主和平台管理员可以关闭会话
        if (!(IMUserService.USER_TYPE_CUSTOMER.equals(operator.getUserType())
                || IMUserService.USER_TYPE_MANAGER.equals(operator.getUserType()))) {
            throw new IllegalParamException("`operatorType`无效值");
        }

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        UserInfo operatorInfo = userInfoDao.find(operator.getUserId(), operator.getUserType(), true);
        if (operatorInfo == null) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        }
        // 根据操作人设置关闭原因
        if (operatorInfo.isCustomer()) {
            if (!operatorInfo.getUserId().equals(session.getUserId())) {
                return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS); // 业主只能关闭自己创建的会话
            }
            session.setClosedReason(SessionClosedReason.USER_CLOSED.getCode());
        } else {
            session.setClosedReason(SessionClosedReason.SERVANT_CLOSED.getCode());
        }

        // 更新会话状态和关闭原因
        session.setAvailable(false);
        session.setUpdateUserId(operator.getUserId());
        session.setClosedUserId(operator.getUserId());
        session.setUpdateTime(Calendar.getInstance().getTime());
        sessionDao.update(session);

        // 如果是由用户关闭的会话, 则向其他成员推送一条退群通知
        if (SessionClosedReason.USER_CLOSED.compare(session.getClosedReason())) {
            pushMemberActionNotice(session, operatorInfo, GroupAction.QUIT);
        }
        // 向IMServer发送解散请求
        imClient.removeGroup(session.getGroupId(), new UserIdentifier(session.getUserId(), session.getUserType()));

        return ApiResult.succ();
    }

    @Override
    public ApiResult<NullObject> dismiss(
            Long sessionId, SessionClosedReason closedReason) throws IMServerException {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        if (!(SessionClosedReason.CLEAR_TEMP_SESSION == closedReason
                || SessionClosedReason.CLEAR_INACTIVE_SESSION == closedReason)) {
            throw new ValidationException("会话关闭原因不正确: " + closedReason);
        }

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }

        // 更新会话状态和关闭原因
        session.setAvailable(false);
        session.setUpdateTime(Calendar.getInstance().getTime());
        session.setClosedReason(SessionClosedReason.USER_CLOSED.getCode());
        sessionDao.update(session);
        // 向IMServer发送解散请求
        imClient.removeGroup(session.getGroupId(),
                new UserIdentifier(session.getUserId(), session.getUserType())); // 以会话创建者的名义解散群组

        return ApiResult.succ();
    }

    @Override
    public ApiResult<Boolean> rename(Long sessionId, String newSessionName,
                                     UserInfoDto operator) throws IMServerException {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(operator, "operator");
        operator.check("operator");
        FieldChecker.checkEmpty(newSessionName, "newSessionName");
        if (newSessionName.length() < SESSION_NAME_MIN_LENGTH
                || newSessionName.length() > SESSION_NAME_MAX_LENGTH) {
            throw new IllegalParamException("群名称长度必须在"
                    + SESSION_NAME_MIN_LENGTH + "-" + SESSION_NAME_MAX_LENGTH + "位以内");
        }

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        if (!session.isAvailable()) {
            return ApiResult.fail(IMErrorCode.SESSION_IS_CLOSED);
        }
        if (session.getName().equals(newSessionName)) {
            return ApiResult.succ(Boolean.FALSE);
        }

        SessionMember member = sessionMemberDao.find(
                sessionId, operator.getUserId(), operator.getUserType());
        if (member == null || !member.isAvailable()) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }
        // 每个成员每天只能修改一次群名称(同一个会话，不包括客服)
        if (!member.isServant() && member.getLastRenameTime() != null
                && SimpleDateUtils.isToday(member.getLastRenameTime())) {
            throw new ValidationException("每个成员每天只能修改一次群名称");
        }

        session.setName(newSessionName);
        session.setUpdateUserId(operator.getUserId());
        session.setUpdateTime(Calendar.getInstance().getTime());
        sessionDao.update(session);
        member.setLastRenameTime(session.getUpdateTime());
        sessionMemberDao.update(member);

        // 向IMServer发送刷新请求
        imClient.refreshGroup(session);
        // 向成员推送群名更改的通知
        pushGroupRenameNotice(session, UserInfo.convertBy(operator));

        return ApiResult.succ(Boolean.TRUE);
    }

    @Override
    public boolean incrementCallTotal(Long sessionId, Long employeeId) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(employeeId, "employeeId");

        SessionMember member = sessionMemberDao.find(
                sessionId, employeeId, IMUserService.USER_TYPE_EMPLOYEE);
        if (member == null || !member.isAvailable()) {
            logger.warn("更新会话呼叫次数失败！sessionId = {}, employeeId = {}, member: {}", sessionId, employeeId, member);
            return false;
        }

        sessionDao.incrementCallTotal(sessionId);
        if (member.getCompanyId() != null) { // 平台业者没有所属公司
            sessionCompanyDao.incrementCallTotal(sessionId, member.getCompanyId());
        }
        sessionMemberDao.incrementCallTotal(sessionId, employeeId, IMUserService.USER_TYPE_EMPLOYEE);
        return true;
    }

    @Override
    public ApiResult<Boolean> markFirstMsgSender(String groupId, UserIdentifier sender) {
        FieldChecker.checkEmpty(groupId, "groupId");
        FieldChecker.checkEmpty(sender, "sender");
        sender.check("sender");

        Session session = sessionDao.getByGroupId(groupId);
        if (session == null) {
            /* TODO：WB 临时处理往后需要优化修改
             * 原因：IM群组会话创建成功后，会发送一条问候语（普通消息），客户端在接收到一条普通消息后，会调
             * 用此接口来标记当前群组的第一条消息发送者。然而，此时创建群组会话的事务数据库还没有执行完成，
             * 导致此处在查询时，有概率会查询不到会话信息，所以临时处理：让当前处理请求等待500毫秒。
             */
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                return ApiResult.succ(false);
            }
            session = sessionDao.getByGroupId(groupId);
            if (session == null) {
                return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
            }
        }
        if (!session.isAvailable() || session.getFirstMsgUserId() != null) {
            return ApiResult.succ(Boolean.FALSE);
        }

        SessionMember sessionMember = sessionMemberDao.find(
                session.getId(), sender.getUserId(), sender.getUserType());
        if (sessionMember == null || !sessionMember.isAvailable()) {
            return ApiResult.fail(IMErrorCode.MEMBER_NOT_JOIN);
        }

        // 家居顾问发送的消息不记录为会话首条消息
        if (IMUserService.USER_TYPE_EMPLOYEE.equals(sender.getUserType())) {
            UserInfo senderInfo = userInfoDao.find(sender.getUserId(), sender.getUserType(), null);
            if (senderInfo.getUserDetailType() != null
                    && senderInfo.getUserDetailType() == IMUserService.PLATFORM_EMPLOYEE_TYPE) {
                return ApiResult.succ(Boolean.FALSE);
            }
        }

        session.setFirstMsgUserId(sender.getUserId());
        sessionDao.update(session);

        // 如果第一条消息的发送者即创建者，则向会话业者发送短信提醒和站内信
        if (session.isCreator(sender)) {
            List<SessionMemberDto> members = sessionMemberDao.findMembersBySessionId(session.getId(), true, true);
            String content = getFirstNoticeContent(session, members);
            if (!Strings.isNullOrEmpty(session.getExclusiveTwitter())) {
                content += noticeTemplate.formatSessionFirstMsgContentExtend(
                        session.getExclusiveTwitter().split(",")[2]);
            }

            InternalMessageDto message = new InternalMessageDto();
            message.setType(InternalMessageType.SESSION.getCode());
            message.setTitle(noticeTemplate.getSessionFirstMsgTitle());
            message.setContent(content);
            message.setExtendInfo(session.getName());
            message.setSendTime(Calendar.getInstance().getTime());
            message.setTargetId(Long.parseLong(session.getGroupId()));
            // 只向开启了`通知提醒`的公司下属的业者发送
            List<String> receivers = members.stream()
                    .filter(SessionMemberDto::isReceiveNotice)
                    .map(SessionMemberDto::getImUid).distinct().collect(Collectors.toList());
            if (receivers != null && !receivers.isEmpty()) {
                logger.debug("向以下业者发送IM咨询的站内通知：" + String.join(",", receivers));
                imMessageService.sendInternalMessageForBatch(message, receivers);
            }

            if (isSmsEnable()) {
                // 只向开启了`短信提醒`的公司下属的业者发送
                List<String> phoneNumbers = members.stream()
                        .filter(member -> UserJoinSessionType.SERVANT.getCode() == member.getUserJoinType()
                                || member.isReceiveSms())
                        .map(SessionMemberDto::getPhone).distinct().collect(Collectors.toList());
                if (phoneNumbers == null) {
                    phoneNumbers = Lists.newArrayList();
                }
                phoneNumbers.add("18696599440"); // 夏士闳
                if (!CollectionUtils.isEmpty(phoneNumbers)) {
                    logger.debug("向以下业者发送IM咨询的短信提醒：" + String.join(",", phoneNumbers));
                    ApiResult<List<String>> result = smsService.sendMessageBatch(phoneNumbers, content);
                    if (result.getData() != null && !result.getData().isEmpty()) {
                        logger.warn("向业者发送IM短信提醒失败！发送失败的手机号：" + String.join(",", result.getData()));
                    }
                }
            }
        }

        return ApiResult.succ(Boolean.TRUE);
    }

    @Override
    public ApiResult<NullObject> markWarningOrNormal(Long sessionId, UserIdentifier operator, byte warning) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(operator, "operator");
        operator.check("operator");

        Session session = sessionDao.get(sessionId);
        if (session == null) {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }

        // 目前只有客服可以执行会话标记, 未来可能商家也可以执行标记
        UserInfo operatorInfo = userInfoDao.find(operator.getUserId(), operator.getUserType(), true);
        if (operatorInfo == null || !operatorInfo.isManager()) {
            return ApiResult.fail(CommonErrorCode.NO_PERMISSIONS);
        }

        if (session.getWarning() != warning) {
            session.setWarning(warning);
            session.setUpdateUserId(operator.getUserId());
            session.setUpdateTime(Calendar.getInstance().getTime());
            sessionDao.update(session);
        }
        return ApiResult.succ();
    }

    @Override
    public Long getSessionIdByUserCase(UserIdentifier user, Long caseId) {
        SessionCase sessionCase = sessionCaseDao.findUserCaseSession(
                user.getUserId(), user.getUserType(), caseId, true);
        return sessionCase != null ? sessionCase.getSessionId() : null;
    }

    @Override
    public String getSessionGroupIdByUserCase(UserIdentifier user, Long caseId) {
        SessionCase sessionCase = sessionCaseDao.findUserCaseSession(
                user.getUserId(), user.getUserType(), caseId, true);
        return sessionCase != null ? sessionCase.getGroupId() : null;
    }

    @Override
    public List<UserIdentifier> findMembersIdentifier(Long sessionId) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        return sessionMemberDao.findMembersIdentifier(sessionId);
    }

    @Override
    public List<SessionMemberDto> findMembers(Long sessionId, boolean queryCompany) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        return sessionMemberDao.findMembersBySessionId(sessionId, true, queryCompany);
    }


    @Override
    public SessionDto findSession(Long sessionId, SessionCommonCondition condition) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(condition, "condition");
        SessionDto sessionDto = sessionDao.find(
                sessionId, condition.getAvailable(), condition.getWarning());
        if (sessionDto != null && condition.isQueryMembers()) {
            List<SessionMemberDto> members = sessionMemberDao.findMembersBySessionId(
                    sessionId, condition.isAllMembers() ? null : true, true);
            sessionDto.setMembers(members);
        }
        return sessionDto;
    }

    @Override
    public SessionDto findSessionByGroupId(String groupId, SessionCommonCondition condition) {
        FieldChecker.checkEmpty(groupId, "groupId");
        FieldChecker.checkEmpty(condition, "condition");
        SessionDto sessionDto = sessionDao.findByGroupId(
                groupId, condition.getAvailable(), condition.getWarning());
        if (sessionDto != null && condition.isQueryMembers()) {
            List<SessionMemberDto> members = sessionMemberDao.findMembersBySessionId(
                    sessionDto.getId(), condition.isAllMembers() ? null : true, true);
            sessionDto.setMembers(members);
        }
        return sessionDto;
    }

    @Override
    public Pagination<SessionDto> querySessions(SessionCommonCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(condition, "condition");
        if (orderHelper != null) {
            orderHelper.validate(SessionOrderField.getInterface());
        }

        int count = sessionDao.getSessionCount(condition);
        Pagination<SessionDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionDto> sessions = sessionDao.querySessions(condition, limitHelper, orderHelper);
            if (!CollectionUtils.isEmpty(sessions)) {
                sessions.forEach(session -> {
                    if (session != null && condition.isQueryMembers()) {
                        List<SessionMemberDto> members = sessionMemberDao.findMembersBySessionId(
                                session.getId(), condition.isAllMembers() ? null : true, true);
                        session.setMembers(members);
                    }
                });
            }
            pagination.setResult(sessions);
        }

        return pagination;
    }

    @Override
    public Pagination<SessionConsoleListDto> consoleQuerySession(SessionConsoleCondition condition, LimitHelper limitHelper, OrderHelper orderHelper, Long userId){
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(condition, "condition");
        if (orderHelper != null) {
            orderHelper.validate(ConsoleSessionOrderField.getInterface());
        }
        int count = sessionDao.findListConsoleCount(condition);
        Pagination<SessionConsoleListDto> pagination = Pagination.newInstance(limitHelper, count);
        if(count>0){
            List<SessionConsoleListDto> sessions = sessionDao.findListConsole(condition,limitHelper,orderHelper);
            if(!CollectionUtils.isEmpty(sessions)){
                sessions.forEach(e->{
                    SessionMember sessionMember = sessionMemberDao.find(e.getId(),userId,"m");
                    //M端管理员没加入这个会话 返回给客户端群聊创建者的token和uid
                    if(sessionMember==null){
                        e.setJoinState(2);
                        SessionUserTokenDto sessionUserTokenDto = userTokenDao.findTokenAndUid(e.getId());//查找群聊创建者
                        if(sessionUserTokenDto!=null) {
                            e.setImToken(org.apache.commons.lang3.StringUtils.isNotBlank(sessionUserTokenDto.getToken()) ? sessionUserTokenDto.getToken() : "");
                            e.setImUid(org.apache.commons.lang3.StringUtils.isNotBlank(sessionUserTokenDto.getUid()) ? sessionUserTokenDto.getUid() : "");
                        }
                        e.setJoinStateDesc("未加入");
                    }else{
                        e.setJoinState(1);
                        e.setJoinStateDesc("已加入");
                    }
                    if(e.getState()==1){
                        e.setStateDesc("未关闭");
                    }else{
                        e.setStateDesc("已关闭");
                    }
                    if(e.getGroupState()==2){
                        e.setGroupStateDesc("风险");
                    }else if(e.getGroupState()==1){
                        e.setGroupStateDesc("重要");
                    }else{
                        e.setGroupStateDesc("正常");
                    }
                });
            }
            pagination.setResult(sessions);
        }
        return pagination;
    }

    @Override
    public ApiResult<SessionConsoleListDto> querySessionInfo(Long sessionId, Long userId) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(userId, "userId");

        SessionConsoleListDto sessionConsoleListDto = sessionDao.querySessionInfo(sessionId);
        if (sessionConsoleListDto != null){
            SessionMember sessionMember = sessionMemberDao.find(sessionConsoleListDto.getId(),userId,"m");
            //M端管理员没加入这个会话 返回给客户端群聊创建者的token和uid
            if(sessionMember==null){
                sessionConsoleListDto.setJoinState(2);
                SessionUserTokenDto sessionUserTokenDto = userTokenDao.findTokenAndUid(sessionConsoleListDto.getId());//查找群聊创建者
                if(sessionUserTokenDto!=null) {
                    sessionConsoleListDto.setImToken(org.apache.commons.lang3.StringUtils.isNotBlank(sessionUserTokenDto.getToken()) ? sessionUserTokenDto.getToken() : "");
                    sessionConsoleListDto.setImUid(org.apache.commons.lang3.StringUtils.isNotBlank(sessionUserTokenDto.getUid()) ? sessionUserTokenDto.getUid() : "");
                }
                sessionConsoleListDto.setJoinStateDesc("未加入");
            }else{
                sessionConsoleListDto.setJoinState(1);
                sessionConsoleListDto.setJoinStateDesc("已加入");
            }
            if(sessionConsoleListDto.getState()==1){
                sessionConsoleListDto.setStateDesc("未关闭");
            }else{
                sessionConsoleListDto.setStateDesc("已关闭");
            }
            if(sessionConsoleListDto.getGroupState()==2){
                sessionConsoleListDto.setGroupStateDesc("风险");
            }else if(sessionConsoleListDto.getGroupState()==1){
                sessionConsoleListDto.setGroupStateDesc("重要");
            }else{
                sessionConsoleListDto.setGroupStateDesc("正常");
            }
        }else {
            return ApiResult.fail(IMErrorCode.SESSION_NOT_EXISTED);
        }
        return ApiResult.succ(sessionConsoleListDto);
    }

    @Override
    public Pagination<SessionDto> findSessionClosedList(
            SessionClosedCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(condition, "condition");
        if (orderHelper != null) {
            orderHelper.validate(SessionOrderField.getInterface());
        }

        int count = sessionDao.getClosedTotal(
                condition.getCompanyId(), condition.getReasonValue(), condition.getWarning());
        Pagination<SessionDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionDto> list = sessionDao.findClosedList(
                    condition.getCompanyId(), condition.getReasonValue(),
                    condition.getWarning(), limitHelper, orderHelper);
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public Pagination<SessionDto> findSessionListByUser(
            UserIdentifier user, Long companyId, SessionCommonCondition condition,
            LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(user, "user");
        user.check("user");
        if (orderHelper != null) {
            orderHelper.validate(SessionOrderField.getInterface());
        }

        int count = sessionDao.getTotalByUser(user, companyId, condition);
        Pagination<SessionDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionDto> list = sessionDao.findListByUser(
                    user, companyId, condition, limitHelper, orderHelper);
            if (condition.isQueryMembers()) {
                getAndSetMembers(list, condition.isAllMembers());
            }
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public Pagination<SessionDto> findSessionListByMember(
            UserIdentifier member, UserJoinSessionType joinType, SessionCommonCondition condition,
            LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(member, "member");
        member.check("member");
        if (orderHelper != null) {
            orderHelper.validate(SessionOrderField.getInterface());
        }

        int count = sessionDao.getTotalByMember(
                member, joinType != null ? joinType.getCode() : null, condition);
        Pagination<SessionDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionDto> list = sessionDao.findListByMember(member,
                    joinType != null ? joinType.getCode() : null, condition, limitHelper, orderHelper);
            if (condition.isQueryMembers()) {
                getAndSetMembers(list, condition.isAllMembers());
            }
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public Pagination<SessionDto> findListNotServant(
            Boolean warning, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(SessionOrderField.getInterface());
        }

        int count = sessionDao.getTotalByNotServant(warning);
        Pagination<SessionDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionDto> list = sessionDao.findListNotServant(warning, limitHelper, orderHelper);
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public List<Long> findTempSessionIds() {
        return sessionDao.findTempSessionIds();
    }

    @Override
    public List<Long> findInactiveSessionIds() {
        return sessionDao.findInactiveSessionIds(imClient.getInactiveSessionDay());
    }

    @Override
    public List<Long> querySessionIdsByUser(UserIdentifier user, Boolean available) {
        FieldChecker.checkEmpty(user, "user");

        return sessionMemberDao.querySessionIdsByUser(user, available);
    }

    @Override
    public UserInfoDto findPlatformEmployee(Long caseId, Long companyId) {
        FieldChecker.checkEmpty(caseId, "caseId");
        FieldChecker.checkEmpty(companyId, "companyId");
        CompanyInfo company = companyInfoDao.findByCompanyId(companyId);
        if (company == null) {
            logger.error("没有找到案例所属的公司信息！caseId = {}, companyId = {}", caseId, companyId);
            return null;
        }
        ApiResult<AreaDto> areaRs = areaService.getLeaf(company.getAreaId());
        if (!areaRs.isSucc()) {
            logger.error("没有找到案例所属公司的地区信息：" + areaRs.getMessage());
            return null;
        }
        // 当前版本将入驻会话的平台业者进行硬编码实现
        UserInfo platformUser;
        if (areaRs.getData().getNamePath().startsWith("重庆")) {
            platformUser = userInfoDao.findByPhone("13452157067", IMUserService.USER_TYPE_EMPLOYEE, true); // 陈松涛
        } else {
            platformUser = userInfoDao.findByPhone("13540498341", IMUserService.USER_TYPE_EMPLOYEE, true); // 卢一慧
        }
        if (platformUser == null) {
            logger.error("没有找到到入驻会话的平台业者！caseId = {}, companyId = {}, areaId = {}",
                    caseId, companyId, company.getAreaId());
            return null;
        } else {
            UserInfoDto platformUserDto = new UserInfoDto();
            BeanUtils.copyProperties(platformUser, platformUserDto);
            return platformUserDto;
        }
    }

    @Override
    public Boolean pushOrderBillPaySuccessNotice(Long sessionId, UserIdentifier sendUser, String msgContent) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(sendUser, "sendUser");
        FieldChecker.checkEmpty(msgContent, "msgContent");
        SessionDto session = sessionDao.find(sessionId, true, null);
        if (session == null) {
            logger.error("订单账单支付成功的小灰条信息发送失败。原因：会话不存在或已关闭！");
            return false;
        }
        if (StringUtils.isEmpty(session.getGroupId())) {
            logger.error("群组会话异常，IM Server不存在此群组会话");
            return false;
        }
        return imClient.sendGroupNotice(session.getGroupId(), sendUser, msgContent);
    }

    private boolean isSmsEnable() {
        return Boolean.TRUE.toString().equalsIgnoreCase(smsSendSwitch);
    }

    private void checkCreate(SessionBuildDto buildDto) {
        FieldChecker.checkEmpty(buildDto, "buildDto");
        buildDto.check("buildDto");

        if (buildDto instanceof SessionBuildByCase) {
            // 每个用户针对相同案例, 只能创建一个未关闭的会话
            SessionBuildByCase caseSession = (SessionBuildByCase) buildDto;
            Long existSessionId = getSessionIdByUserCase(
                    caseSession.getCreator().buildUserIdentifier(), caseSession.getCaseId());
            if (existSessionId != null) {
                throw new ValidationException(
                        IMErrorCode.SESSION_EXISTED.getMessage() + "(" + existSessionId + ")");
            }
        }

        // 检查会话发起者以及团队中的任一业者是否达到会话上限
        checkOperatorSessionLimit(
                buildDto.getCreator().getUserId(), buildDto.getCreator().getUserType());
        checkMembersSessionLimit(buildDto.getMembers(), IMUserService.USER_TYPE_EMPLOYEE);
    }

    private void checkAddMembers(
            Long sessionId, Set<Long> membersId, String memberType, UserIdentifier operator) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(membersId, "membersId");
        FieldChecker.checkEmpty(operator, "operator");
        operator.check("operator");

        // 根据操作人类型判断其可以拉入会话的成员类型
        if (IMUserService.USER_TYPE_EMPLOYEE.equals(operator.getUserType())) {
            if (!IMUserService.USER_TYPE_EMPLOYEE.equals(memberType)) {
                throw new ValidationException("只能添加业者！");
            }
        } else {
            if (!(IMUserService.USER_TYPE_CUSTOMER.equals(memberType)
                    || IMUserService.USER_TYPE_EMPLOYEE.equals(memberType))) {
                throw new ValidationException("只能添加业主或业者！");
            }
        }
    }

    // 格式化群组头像
    private String formatSessionIcon(SessionBuildDto buildDto) {
        String sessionIcon;
        if (buildDto instanceof SessionBuildByCase) {
            // 从作品创建的会话，群头像默认为作品的封面图。因作品封面尺寸过大，需要更换尺寸
            sessionIcon = StringUtils.split(((SessionBuildByCase) buildDto).getCaseCover(), "?")[0];
            sessionIcon += "?width=120&height=120";
            logger.info("会话{}的头像{}", buildDto.getSessionName(), sessionIcon);
        } else {
            sessionIcon = DEFAULT_GROUP_IMAGE;
        }

        return sessionIcon;
    }

    private void checkRemoveMembers(Long sessionId, UserIdentifier member) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(member, "member");
        member.check("member");
        if (IMUserService.USER_TYPE_MANAGER.equals(member.getUserType())) {
            throw new ValidationException("不能移除客服！");
        }
    }

    private void checkOperatorSessionLimit(Long userId, String userType) {
        int count = sessionDao.getTotalByUserJoined(userId, userType);
        if (count >= imClient.getUserMaxSession()) {
            throw new ValidationException("您的聊天群已到达上限，请解散不必要的聊天");
        }
    }

    private void checkMembersSessionLimit(List<UserInfo> members, String userType) {
        Set<SessionMemberDto> memberDtos = new LinkedHashSet<>(members.size());
        for (UserInfo userInfo : members) {
            SessionMemberDto member = new SessionMemberDto();
            member.setUserId(userInfo.getUserId());
            member.setUserType(userType);
            member.setShowName(userInfo.getShowName());
            memberDtos.add(member);
        }
        checkMembersSessionLimit(memberDtos, userType);
    }

    private void checkMembersSessionLimit(Set<SessionMemberDto> members, String userType) {
        List<Map<String, Object>> list = sessionDao.getTotalByMultiUserJoined(
                members.stream().map(member -> member.getUserId()).collect(Collectors.toList()), userType);
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                if (((Long) map.get("sessionTotal")).intValue() >= imClient.getUserMaxSession()) {
                    Long userId = (Long) map.get("userId");
                    String memberName = null;
                    for (SessionMemberDto member : members) {
                        if (member.getUserId().equals(userId)) {
                            memberName = member.getShowName();
                            break;
                        }
                    }
                    throw new ValidationException(memberName + "当前聊天群过多，请选择其他用户");
                }
            }
        }
    }

    private Session saveSession(SessionBuildDto buildDto) {
        // 创建并持久化会话信息
        Session session = new Session();
        session.setUserId(buildDto.getCreator().getUserId());
        session.setUserType(buildDto.getCreator().getUserType());
        session.setName(buildDto.getSessionName());
        session.setAvailable(true);
        session.setExclusiveTwitter(buildDto.getExclusiveTwitter());
        session.setCreateTime(Calendar.getInstance().getTime());
        session.setUpdateTime(session.getCreateTime());
        sessionDao.save(session);

        if (buildDto instanceof SessionBuildByCase) {
            saveSessionCase((SessionBuildByCase) buildDto, session);
        }

        return session;
    }

    private void saveSessionCase(SessionBuildByCase buildDto, Session session) {
        // 创建并持久化会话关联的案例
        SessionCase sessionCase = new SessionCase();
        sessionCase.setSessionId(session.getId());
        sessionCase.setCaseId(buildDto.getCaseId());
        sessionCase.setCaseName(buildDto.getCaseName());
        sessionCaseDao.save(sessionCase);
    }

    private void saveSessionCompanies(SessionBuildDto buildDto, Session session) {
        // 创建并持久化会话关联的公司
        List<Long> companiesId = buildDto.distinctCompanyIdOfMembers();
        List<SessionCompany> sessionCompanies = companiesId.stream()
                .map(companyId -> createSessionCompanyEntity(session.getId(), companyId))
                .collect(Collectors.toList());
        sessionCompanyDao.saveBatch(sessionCompanies);
        // 统计关联公司的会话创建数量
        imStatisticsService.incrementCompaniesIMSession(companiesId);
    }

    private void addSessionCompanies(List<UserInfo> members, Session session) {
        // 获取成员业者所属的公司ID
        List<Long> companiesId = members.stream()
                .filter(member -> member.isEmployee() && member.getCompanyId() != null)
                .map(UserInfo::getCompanyId).distinct().collect(Collectors.toList());
        if (companiesId != null && !companiesId.isEmpty()) {
            // 剔除已有关系的公司ID
            List<SessionCompany> existCompanies = sessionCompanyDao.findCompanies(session.getId(), companiesId);
            if (existCompanies != null && !existCompanies.isEmpty()) {
                for (Iterator<Long> it = companiesId.iterator(); it.hasNext(); ) {
                    for (SessionCompany sessionCompany : existCompanies) {
                        if (it.next().equals(sessionCompany.getCompanyId())) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
            if (!companiesId.isEmpty()) {
                List<SessionCompany> sessionCompanies = companiesId.stream()
                        .map(companyId -> createSessionCompanyEntity(session.getId(), companyId))
                        .collect(Collectors.toList());
                sessionCompanyDao.saveBatch(sessionCompanies);
                // 统计关联公司的会话创建数量
                imStatisticsService.incrementCompaniesIMSession(companiesId);
            }
        }
    }

    private List<SessionMember> saveSessionMembers(
            SessionBuildDto buildDto, Session session, UserInfoDto platformEmployee) {
        // 创建并持久化会话成员
        List<SessionMember> sessionMembers = new ArrayList<>();
        sessionMembers.add(createSessionMemberEntity(session.getId(),
                session.getUserId(), session.getUserType(), null,
                UserJoinSessionType.CREATOR, session.getCreateTime()));

        buildDto.getMembers().forEach(member -> sessionMembers.add(
                createSessionMemberEntity(
                        session.getId(), member.getUserId(),
                        member.getUserType(), member.getCompanyId(),
                        UserJoinSessionType.MEMBERS, session.getCreateTime())));

        // 添加入驻会话的平台业者
        if (platformEmployee != null) {
            SessionPlatformEmployee relation = new SessionPlatformEmployee();
            relation.setSessionId(session.getId());
            relation.setPeUserId(platformEmployee.getUserId());
            relation.setJoined(Boolean.TRUE);
            int sessionTotal = sessionDao.getTotalByUserJoined(platformEmployee.getUserId(), "e");
            int maxSessionCount = imClient.getUserMaxSession();
            if (sessionTotal >= maxSessionCount) {
                logger.error("入驻会话的平台家居顾问当前聊天数已达上限！userId = {}", platformEmployee.getUserId());
                smsService.sendMessage(platformEmployee.getPhone(),
                        platformEmployee.getShowName() + "，你的聊天群已达上限，无法以平台家居顾问的身份入驻，请退出不必要的聊天");
                relation.setJoined(Boolean.FALSE);
                // 发送已达到最大会话数的通知短信
                String msgContent = String.format(peSessionCountLimitHundred, platformEmployee.getShowName());
                smsService.sendMessage(platformEmployee.getPhone(), msgContent);
            } else {
                if (sessionTotal >= (maxSessionCount * 0.8f)) {
                    // 发送已达到最大会话数的80%通知短信
                    String msgContent = String.format(peSessionCountLimitEighty, platformEmployee.getShowName(), sessionTotal, maxSessionCount);
                    smsService.sendMessage(platformEmployee.getPhone(), msgContent);
                }
                sessionMembers.add(createSessionMemberEntity(session.getId(),
                        platformEmployee.getUserId(), platformEmployee.getUserType(), null,
                        UserJoinSessionType.MEMBERS, session.getCreateTime()));
            }
            sessionPlatformEmployeeDao.save(relation);
        }

        sessionMemberDao.saveBatch(sessionMembers);
        return sessionMembers;
    }

    private List<SessionMember> addOrUpdateMembers(Session session,
                                                   List<UserInfo> newMembers, List<SessionMember> oldMembers) {
        Date nowTime = Calendar.getInstance().getTime();
        List<SessionMember> allMembers = new ArrayList<>(newMembers.size() + oldMembers.size());
        // 更新已有成员
        if (!oldMembers.isEmpty()) {
            oldMembers.forEach(member -> {
                member.setAvailable(true);
                member.setUpdateTime(nowTime);
                sessionMemberDao.update(member);
                allMembers.add(member);
            });
        }
        // 添加新进成员
        if (!newMembers.isEmpty()) {
            List<SessionMember> entities = newMembers.stream()
                    .map(member -> createSessionMemberEntity(
                            session.getId(), member.getUserId(),
                            member.getUserType(), member.getCompanyId(),
                            UserJoinSessionType.MEMBERS, nowTime)).collect(Collectors.toList());
            sessionMemberDao.saveBatch(entities);
            allMembers.addAll(entities);
            addSessionCompanies(newMembers, session);
        }
        return allMembers;
    }

    private void getAndSetMembers(List<SessionDto> sessionList, boolean allMembers) {
        Long[] sessionIds = new Long[sessionList.size()];
        for (int i = 0; i < sessionList.size(); i++) {
            sessionIds[i] = sessionList.get(i).getId();
        }
        // 查询所有会话的成员列表, 并分别填充到每个SessionDto
        List<SessionMemberDto> members = sessionMemberDao.findMembersInSessionIds(
                sessionIds, allMembers ? null : true, true);
        if (members != null && !members.isEmpty()) {
            for (SessionDto session : sessionList) {
                session.setMembers(new ArrayList<>());
                for (Iterator<SessionMemberDto> it = members.iterator(); it.hasNext(); ) {
                    SessionMemberDto member = it.next();
                    if (member.getSessionId().equals(session.getId())) {
                        session.getMembers().add(member);
                        it.remove();
                    }
                }
            }
        }
    }

    private SessionCompany createSessionCompanyEntity(Long sessionId, Long companyId) {
        SessionCompany entity = new SessionCompany();
        entity.setSessionId(sessionId);
        entity.setCompanyId(companyId);
        entity.setAvailable(true);
        return entity;
    }

    private SessionMember createSessionMemberEntity(
            Long sessionId, Long userId, String userType, Long companyId,
            UserJoinSessionType userJoinType, Date createTime) {
        SessionMember entity = new SessionMember();
        entity.setSessionId(sessionId);
        entity.setUserId(userId);
        entity.setUserType(userType);
        entity.setCompanyId(companyId);
        entity.setUserJoinType(userJoinType.getCode());
        entity.setAvailable(true);
        entity.setCreateTime(createTime);
        return entity;
    }

    private String getFirstNoticeContent(Session session, List<SessionMemberDto> members) {
        String creatorName = members.stream().filter(member -> member.isCreator())
                .map(member -> member.getShowName()).collect(Collectors.toList()).get(0);
        SessionCase sc = sessionCaseDao.findBySessionId(session.getId());
        String targetName;
        if (sc != null && !Strings.isNullOrEmpty(sc.getCaseName())) {
            targetName = sc.getCaseName();
        } else {
            targetName = session.getName();
        }
        return noticeTemplate.formatSessionFirstMsgContent(creatorName, targetName);
    }

    private void pushGroupCreatedNotice(Session session) {
        String msg = noticeTemplate.getSessionCreated();
        imClient.sendGroupNotice(session.getGroupId(), msg);
    }

    private void pushGroupRenameNotice(Session session, UserInfo operator) {
        String msg = noticeTemplate.formatSessionRename(operator.getFullName(), session.getName());
        imClient.sendGroupNotice(session.getGroupId(), msg, MessageIdentifier.GROUP_RENAME);
    }

    private void pushMemberActionNotice(Session session, UserInfo member, GroupAction action) {
        if (!(GroupAction.JOIN == action || GroupAction.QUIT == action)) {
            throw new IllegalArgumentException("action可选值: [JOIN, QUIT]");
        }
        String msg = member.getFullName() + "已" + action.getName() + "聊天";
        imClient.sendGroupNotice(session.getGroupId(), msg);
    }

    private void pushAddMemberNotice(Session session, List<UserInfo> members, UserInfo operator) {
        String msg = operator.getFullName() + "将" + String.join("、", members.stream()
                .map(member -> member.getShowName()).collect(Collectors.toList())) + "拉入聊天";
        imClient.sendGroupNotice(session.getGroupId(), msg);
    }

    private void pushRemoveMemberNotice(Session session, UserInfo member, UserInfo operator) {
        if (operator == null) {
            pushMemberActionNotice(session, member, GroupAction.QUIT);
        } else {
            String msg = operator.getFullName() + "将" + member.getShowName() + "移出聊天";
            imClient.sendGroupNotice(session.getGroupId(), msg);
        }
    }

    // v1.7.0 修改为获取客户关联的家居顾问
    private UserInfoDto getAndCheckPlatformEmployee(SessionBuildDto buildDto) {
        if (buildDto instanceof SessionBuildByCase) {
            SessionBuildByCase caseBuildDto = (SessionBuildByCase) buildDto;
            //UserInfoDto platformUser = findPlatformEmployee(caseBuildDto.getCaseId(), caseBuildDto.getCompanyId());
            UserInfoDto platformUser = findPlatformEmployee(caseBuildDto.getCaseId(), caseBuildDto.getCompanyId());
            if (platformUser != null) {
                int sessionTotal = sessionDao.getTotalByUserJoined(platformUser.getUserId(), platformUser.getUserType());
                if (sessionTotal >= imClient.getUserMaxSession()) {
                    logger.error("入驻会话的平台业者当前聊天数已达上限！userId = {}", platformUser.getUserId());
                    smsService.sendMessage(platformUser.getPhone(),
                            platformUser.getShowName() + "，你的聊天群已达上限，无法以平台业者的身份入驻，请退出不必要的聊天");
                } else {
                    return platformUser;
                }
            }
        }
        return null;
    }




}