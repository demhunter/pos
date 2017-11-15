/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.exception.InitializationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.constant.MessageIdentifier;
import com.pos.im.constant.MessageReceiverType;
import com.pos.im.domain.Session;
import com.pos.im.domain.SessionMember;
import com.pos.im.dto.message.InternalMessageDto;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.exception.IMErrorCode;
import com.pos.im.exception.IMServerException;
import com.pos.im.service.support.netease.NeteaseIMClient;
import com.pos.im.service.support.netease.dto.request.*;
import com.pos.im.service.support.netease.dto.response.BatchSendMsgResponseDto;
import com.pos.im.service.support.netease.dto.response.CreateResponseDto;
import com.pos.im.service.support.netease.dto.response.CreateTeamResponseDto;
import com.pos.im.service.support.netease.dto.response.OnlyCodeResponseDto;
import com.pos.im.service.support.notice.InternalMessageSystemNotice;
import com.pos.im.service.support.notice.SystemNotice;
import com.pos.im.service.support.notice.TipsMessageSystemNotice;
import com.pos.im.service.support.notice.tips.NormalTips;
import com.pos.im.service.support.notice.tips.Tips;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IM客户端集成工具.
 *
 * @author wayne
 * @version 1.0, 2016/7/14
 */
@Component
public final class IMClient {

    private Logger logger = LoggerFactory.getLogger(IMClient.class);

    @Resource
    private NeteaseIMClient neteaseIMClient;

    @Value("${im.app.key}")
    private String appKey;

    @Value("${im.app.secret}")
    private String appSecret;

    @Value("${im.token.days}")
    private int tokenDays;

    @Value("${im.user.maxSession}")
    private int userMaxSession;

    @Value("${im.session.maxMember}")
    private int sessionMaxMember;

    @Value("${im.session.inactive.day}")
    private int inactiveSessionDay;

    @Value("${im.netease.accid.prefix}")
    private String neteaseAccidPrefix;

    /**
     * IM虚拟用户名, 用于向用户发送系统类通知消息
     */
    private final String virtualSysUser = "ywmj-sys-user";

    @PostConstruct
    public void initialize() {
        try {
            logger.info("向IM Server注册一个虚拟的系统用户.");
            ApiResult<CreateResponseDto> result = neteaseIMClient.refreshToken(virtualSysUser);
            if (!result.isSucc()) {
                InitializationException ex = new InitializationException(
                        "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("向IM Server注册一个虚拟的系统用户失败！", ex);
                throw ex;
            }
        } catch (Exception e) {
            logger.error("向IM Server注册一个虚拟的系统用户失败！", e);
            throw new InitializationException("向IM Server注册一个虚拟的系统用户失败！", e);
        }
    }

    /**
     * 获取用于连接IM Server的APP key.
     *
     * @return
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * 获取用于连接IM Server的APP Secret.
     *
     * @return
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * 获取用户Token的有效期设置(单位: 天).
     *
     * @return
     */
    public int getTokenDays() {
        return tokenDays;
    }

    /**
     * 获取每个用户最多可以加入的会话数量.
     *
     * @return
     */
    public int getUserMaxSession() {
        return userMaxSession;
    }

    /**
     * 获取每个会话最多可以加入的成员数量.
     *
     * @return
     */
    public int getSessionMaxMember() {
        return sessionMaxMember;
    }

    /**
     * 获取不活跃会话的天数定义: 即创建于x天之前(包含x天)且最近x天之内没有产生过聊天的会话视为不活跃.
     *
     * @return
     */
    public int getInactiveSessionDay() {
        return inactiveSessionDay;
    }

    /**
     * 获取一个虚拟的IM系统用户的IM USER_ID.
     *
     * @return
     */
    public String getVirtualSysUser() {
        return virtualSysUser;
    }

    /**
     * 创建IM用户
     *
     * @param userInfo 用户信息
     * @return 创建成功返回IM Token
     * @throws IMServerException 往IM Server注册账号失败
     */
    public String createUser(UserInfoDto userInfo) throws IMServerException {
        CreateRequestDto createRequest = toCreateRequestDto(userInfo);
        ApiResult<CreateResponseDto> result = neteaseIMClient.create(createRequest);
        if (!result.isSucc()) {
            IMServerException exception = new IMServerException(
                    "err: (" + result.getError().getCode() + ") " + result.getMessage());
            logger.error("创建网易云信IM账号失败。用户：" + userInfo.toString() + "。原因：" + result.getMessage(), exception);
            throw exception;
        }
        return result.getData().getInfo().getToken();
    }

    /**
     * 更新IM用户信息
     *
     * @param userInfo 用户信息
     * @throws IMServerException 往IM Server更新用户信息失败
     */
    public void updateUser(UserInfoDto userInfo) throws IMServerException {
        CreateRequestDto createRequest = toCreateRequestDto(userInfo);
        ApiResult result = neteaseIMClient.update(createRequest);
        if (!result.isSucc()) {
            IMServerException exception = new IMServerException(
                    "err: (" + result.getError().getCode() + ") " + result.getMessage());
            logger.error("更新网易云信用户IM信息失败。用户：" + userInfo.toString() + "。原因：" + result.getMessage(), exception);
            throw exception;
        }
    }

    /**
     * 刷新用户IM Token，并返回最新的Token信息
     *
     * @param accid IM唯一通信凭证
     * @return IM token
     * @throws IMServerException 刷新用户IM Token异常
     */
    public String refreshToken(String accid) throws IMServerException {
        ApiResult<CreateResponseDto> result = neteaseIMClient.refreshToken(accid);
        if (!result.isSucc()) {
            IMServerException ex = new IMServerException(
                    "err: (" + result.getError().getCode() + ") " + result.getMessage());
            logger.error("向IM Server获取用户({})Token失败！", accid);
            throw ex;
        }

        return result.getData().getInfo().getToken();
    }

    /**
     * 格式化IM Server accid
     *
     * @param user 用户标识
     * @return accid，格式：prefix-userType-userId
     */
    public String formatAccid(UserIdentifier user) {
        FieldChecker.checkEmpty(user, "user");
        user.check("user");

        return neteaseAccidPrefix + "-" + user.getUserType() + "-" + user.getUserId();
    }

    /**
     * 创建群组会话.
     *
     * @param session 会话信息
     * @param members 成员列表
     * @return 群组会话创建成功，返回相应的群组会话id
     * @throws IMServerException 操作失败
     */
    public String createGroup(Session session, List<SessionMember> members) throws IMServerException {
        // 默认无头像
        return createGroup(session, null, members);
    }

    /**
     * 创建群组会话.
     *
     * @param session     会话信息
     * @param sessionIcon 群组头像
     * @param members     成员列表
     * @return 群组会话创建成功，返回相应的群组会话id
     * @throws IMServerException 操作失败
     */
    public String createGroup(Session session, String sessionIcon, List<SessionMember> members) throws IMServerException {
        try {
            CreateTeamRequestDto teamRequest = toCreateTeamRequestDto(session, sessionIcon, members);
            ApiResult<CreateTeamResponseDto> result = neteaseIMClient.createTeam(teamRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("向IM Server申请创建群组失败！errMsg = {}", ex.getMessage());
                throw ex;
            }
            return result.getData().getTid();
        } catch (Exception e) {
            logger.error("向IM Server申请创建群组失败！", e);
            throw new IMServerException(IMErrorCode.SESSION_CREATE_FAILED.getMessage());
        }
    }

    /**
     * 加入群组会话.
     *
     * @param session 会话信息
     * @param members 新加入成员列表
     * @throws IMServerException 操作失败
     */
    public void addGroupMembers(Session session, List<SessionMember> members) throws IMServerException {
        try {
            TeamAddRequestDto addMembersRequest = toTeamAddRequestDto(session, members);
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.addTeam(addMembersRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("向IM Server申请加入群组失败！errMsg = {}", ex.getMessage());
                throw ex;
            }
        } catch (Exception e) {
            logger.error("向IM Server申请加入群组失败！", e);
            throw new IMServerException(IMErrorCode.SESSION_JOIN_FAILED.getMessage());
        }
    }

    /**
     * 移除指定群组会话.
     *
     * @param groupId 群组id
     * @param owner   群组所有人用户标识
     * @throws IMServerException 操作失败
     */
    public void removeGroup(String groupId, UserIdentifier owner) throws IMServerException {
        try {
            TeamRemoveRequestDto removeRequest = new TeamRemoveRequestDto();
            removeRequest.setTid(groupId);
            removeRequest.setOwner(formatAccid(owner));
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.removeTeam(removeRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("向IM Server申请解散群组失败！groupId = {}, owner = {}, errMsg = {}",
                        groupId, owner.toString(), ex.getMessage());
                throw ex;
            }
        } catch (Exception e) {
            logger.error("向IM Server申请解散群组失败！" +
                    "groupId = " + groupId + ", owner = " + owner.toString(), e);
            throw new IMServerException(IMErrorCode.SESSION_CLOSE_FAILED.getMessage());
        }
    }

    /**
     * 退出指定群组会话.
     *
     * @param groupId    群组会话id
     * @param quitMember 要退出群组的成员用户标识
     * @throws IMServerException 操作失败
     */
    public void quitGroup(String groupId, UserIdentifier quitMember) throws IMServerException {
        try {
            LeaveTeamRequestDto leaveRequest = new LeaveTeamRequestDto();
            leaveRequest.setAccid(formatAccid(quitMember));
            leaveRequest.setTid(groupId);
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.leaveTeam(leaveRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError() + ") " + result.getMessage());
                logger.error("向IM Server申请退出群组失败！groupId = {}, quitMember = {}, errMsg = {}",
                        groupId, quitMember.toString(), ex.getMessage());
                throw ex;
            }
        } catch (Exception e) {
            logger.error("向IM Server申请退出群组失败！" +
                    "groupId = " + groupId + ", quitMember = " + quitMember.toString(), e);
            throw new IMServerException(IMErrorCode.SESSION_QUIT_FAILED.getMessage());
        }
    }

    /**
     * 移除指定群组会话的成员.
     *
     * @param groupId      群组会话id
     * @param owner        群主用户标识
     * @param removeMember 被移除的会话成员标识
     * @throws IMServerException 操作失败
     */
    public void removeGroupMember(String groupId, UserIdentifier owner, UserIdentifier removeMember) throws IMServerException {
        try {
            TeamKickRequestDto kickRequest = new TeamKickRequestDto();
            kickRequest.setTid(groupId);
            kickRequest.setOwner(formatAccid(owner));
            kickRequest.setMember(formatAccid(removeMember));
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.kickTeam(kickRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError() + ") " + result.getMessage());
                logger.error("向IM Server申请移除群组成员失败！groupId = {}, removeMember = {}, errMsg = {}",
                        groupId, removeMember.toString(), ex.getMessage());
                throw ex;
            }
        } catch (Exception e) {
            logger.error("向IM Server申请移除群组成员失败！" +
                    "groupId = " + groupId + ", removeMember = " + removeMember.toString(), e);
            throw new IMServerException(IMErrorCode.SESSION_QUIT_FAILED.getMessage());
        }
    }

    /**
     * 刷新指定群组会话.
     *
     * @param session 会话信息
     * @throws IMServerException 操作失败
     */
    public void refreshGroup(Session session) throws IMServerException {
        try {
            TeamUpdateRequestDto teamRequest = new TeamUpdateRequestDto();
            teamRequest.setTid(session.getGroupId());
            teamRequest.setOwner(formatAccid(new UserIdentifier(session.getUserId(), session.getUserType())));
            teamRequest.setTname(session.getName());
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.updateTeam(teamRequest);
            if (!result.isSucc()) {
                IMServerException ex = new IMServerException(
                        "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("IM Server刷新群组信息失败！sessionId = {}, errMsg = {}", session.getId(), ex.getMessage());
                throw ex;
            }
        } catch (Exception e) {
            logger.error("IM Server刷新群组信息失败！sessionId = " + session.getId(), e);
            throw new IMServerException(IMErrorCode.SESSION_UPDATE_FAILED.getMessage());
        }
    }

    /**
     * 发送一条群组文本消息.
     *
     * @param groupId    群组会话ID
     * @param sender     发送者用户标识
     * @param msgContent 消息文本内容
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendGroupTextMessage(String groupId, UserIdentifier sender, String msgContent) {
        try {
            SingleSendMsgRequestDto msgRequest = new SingleSendMsgRequestDto();
            msgRequest.setFrom(formatAccid(sender)); // 发送人
            msgRequest.setOpe(1); // 消息接收方类型，群组
            msgRequest.setTo(groupId); // 接收方id
            msgRequest.setType(0); // 消息类型，为文本消息
            Map<String, String> map = new HashMap<>();
            map.put("msg", msgContent);
            JSONObject jsonObject = new JSONObject(map);
            msgRequest.setBody(jsonObject.toString());
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.sendSingleMsg(msgRequest);
            if (!result.isSucc()) {
                String errMsg = "err: (" + result.getError().getCode() + ") " + result.getMessage();
                logger.warn("发送群消息失败！groupId = {}, message = {}, errMsg = {}", groupId, msgContent, errMsg);
                return false;
            }
        } catch (Exception e) {
            logger.warn("发送群消息失败！groupId = " + groupId + ", message = " + msgContent, e);
            return false;
        }
        return true;
    }

    /**
     * 以系统身份发送一条群组通知.
     * <p>
     * 发送成功后指定群组的所有成员都可以收到一个小灰条提示信息, 但如果发送者也是该群成员, 那他自己不会收到该条消息.
     *
     * @param groupId    群组会话ID
     * @param msgContent 通知消息的文本内容
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendGroupNotice(String groupId, String msgContent) {
        return sendGroupNotice(groupId, getVirtualSysUser(), msgContent, null);
    }

    /**
     * 以系统身份发送一条群组通知.
     * <p>
     * 发送成功后指定群组的所有成员都可以收到一个小灰条提示信息, 但如果发送者也是该群成员, 那他自己不会收到该条消息.
     *
     * @param groupId    群组会话ID
     * @param msgContent 通知消息的文本内容
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendGroupNotice(String groupId, String msgContent, MessageIdentifier msgExtra) {
        return sendGroupNotice(groupId, getVirtualSysUser(), msgContent, msgExtra);
    }

    /**
     * 发送一条群组通知.
     * <p>
     * 发送成功后指定群组的所有成员都可以收到一个小灰条提示信息, 但如果发送者也是该群成员, 那他自己不会收到该条消息.
     *
     * @param groupId    群组会话ID
     * @param sender     发送者用户标识
     * @param msgContent 通知消息的文本内容
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendGroupNotice(String groupId, UserIdentifier sender, String msgContent) {
        return sendGroupNotice(groupId, sender, msgContent, null);
    }

    /**
     * 发送一条群组通知.
     * <p>
     * 发送成功后指定群组的所有成员都可以收到一个小灰条提示信息, 但如果发送者也是该群成员, 那他自己不会收到该条消息.
     *
     * @param groupId    群组会话ID
     * @param sender     发送者用户标识
     * @param msgContent 通知消息的文本内容
     * @param msgExtra   通知消息的扩展字段, 用于表示消息来源
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendGroupNotice(String groupId, UserIdentifier sender, String msgContent, MessageIdentifier msgExtra) {
        return sendGroupNotice(groupId, formatAccid(sender), msgContent, msgExtra);
    }

    private boolean sendGroupNotice(String groupId, String senderAccid, String msgContent, MessageIdentifier msgExtra) {
        try {
            Tips tips;
            if (msgExtra == null) {
                tips = new NormalTips(msgContent,groupId);
            } else {
                tips = new NormalTips(msgContent, msgExtra.toString(),groupId);
            }
            SystemNotice notice = new TipsMessageSystemNotice(tips);
            sendSystemNotice(notice, senderAccid, MessageReceiverType.GROUP, groupId);
        } catch (Exception e) {
            logger.warn("发送群通知失败！groupId = " + groupId + ", message = " + msgContent, e);
            return false;
        }
        return true;
    }

    /**
     * 向指定用户推送一条站内信消息
     *
     * @param messageDto 站内信消息
     * @param receiver   接受人用户标识
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendInternalMessage(InternalMessageDto messageDto, UserIdentifier receiver) {
        try {
            // 解析3大消息类型（仅查看，跳转会话，跳转详情）
            messageDto.parseInternalMessageViewType();
            SystemNotice notice = new InternalMessageSystemNotice(messageDto);
            sendSystemNotice(notice, getVirtualSysUser(), MessageReceiverType.PERSON, formatAccid(receiver));
        } catch (Exception e) {
            logger.error("发送站内消息失败！messageDto = " + messageDto, e);
            return false;
        }
        return true;
    }

    /**
     * 向指定用户组推送一条站内信消息
     *
     * @param messageDto 站内信消息
     * @param receivers  接受人用户标识
     * @return 发送成功返回true, 失败返回false
     */
    public boolean sendInternalMessageForBatch(InternalMessageDto messageDto, List<UserIdentifier> receivers) {
        try {
            // 解析3大消息类型（仅查看，跳转会话，跳转详情）
            messageDto.parseInternalMessageViewType();
            SystemNotice notice = new InternalMessageSystemNotice(messageDto);
            List<String> accids = receivers.stream()
                    .map(this::formatAccid).collect(Collectors.toList());
            sendSystemNoticeForBatch(notice, getVirtualSysUser(), accids);
        } catch (Exception e) {
            logger.error("发送站内消息失败！message = " + messageDto, e);
            return false;
        }
        return true;
    }

    /**
     * 发送系统通知
     *
     * @param notice       通知内容
     * @param sender       发送方
     * @param receiverType 接收方类型 {@link MessageReceiverType}
     * @param receiver     接收方
     * @throws IMServerException 系统通知发送失败
     */
    private void sendSystemNotice(SystemNotice notice, String sender,
                                  MessageReceiverType receiverType, String receiver) throws IMServerException {
        try {
            SingleSendAttachMsgRequestDto msgRequest = new SingleSendAttachMsgRequestDto();
            msgRequest.setFrom(sender);
            msgRequest.setMsgtype(receiverType.getCode());
            msgRequest.setTo(receiver);
            msgRequest.setAttach(notice.toString());
            msgRequest.setSave(2);
            ApiResult<OnlyCodeResponseDto> result = neteaseIMClient.sendAttachMsg(msgRequest);
            if (!result.isSucc()) {
                IMServerException exception = new IMServerException("发送自定义系统通知失败！" + "notice = " + notice.toString() + "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("发送系统通知失败！" + "notice = " + notice.toString() + "err: (" + result.getError().getCode() + ") " + result.getMessage());
                throw exception;
            }
        } catch (Exception e) {
            IMServerException exception = new IMServerException("发送自定义系统通知失败！", e);
            logger.error("发送系统通知失败！", e);
            throw exception;
        }
    }

    /**
     * 批量发送系统通知
     *
     * @param notice    通知内容
     * @param sender    发送方
     * @param receivers 接收方列表
     * @throws IMServerException 系统通知发送失败
     */
    private void sendSystemNoticeForBatch(SystemNotice notice, String sender,
                                          List<String> receivers) throws IMServerException {
        try {
            BatchSendAttachMsgRequestDto msgRequest = new BatchSendAttachMsgRequestDto();
            msgRequest.setFromAccid(sender);
            msgRequest.setToAccids(JsonUtils.objectToJson(receivers));
            msgRequest.setAttach(notice.toString());
            ApiResult<BatchSendMsgResponseDto> result = neteaseIMClient.batchSendAttachMsg(msgRequest);
            if (!result.isSucc()) {
                IMServerException exception = new IMServerException("发送自定义系统通知失败！" + "notice = " + notice.toString() + "err: (" + result.getError().getCode() + ") " + result.getMessage());
                logger.error("发送自定义系统通知失败！" + "notice = " + notice.toString() + "err: (" + result.getError().getCode() + ") " + result.getMessage());
                throw exception;
            }
            List<String> failureUsers = result.getData().getUnregister();
            if (!CollectionUtils.isEmpty(failureUsers)) {
                logger.warn("向用户组={}发送系统通知失败！", failureUsers);
            }
        } catch (Exception e) {
            IMServerException exception = new IMServerException("发送自定义系统通知失败！", e);
            logger.error("发送系统通知失败！", e);
            throw exception;
        }
    }

    private CreateRequestDto toCreateRequestDto(UserInfoDto userInfo) {
        CreateRequestDto createRequest = new CreateRequestDto();

        createRequest.setAccid(userInfo.getImUid());
        createRequest.setIcon(userInfo.getShowHead());
        createRequest.setName(userInfo.getShowName());

        return createRequest;
    }

    private CreateTeamRequestDto toCreateTeamRequestDto(Session session, String sessionIcon, List<SessionMember> members) {
        CreateTeamRequestDto teamRequest = new CreateTeamRequestDto();
        teamRequest.setTname(session.getName());
        teamRequest.setOwner(formatAccid(new UserIdentifier(session.getUserId(), session.getUserType())));
        teamRequest.setIcon(sessionIcon);
        List<String> memberAccids = members.stream()
                .map(member -> formatAccid(new UserIdentifier(member.getUserId(), member.getUserType())))
                .collect(Collectors.toList());
        teamRequest.setMsg("您被邀请加入会话：" + session.getName());
        teamRequest.setMembers(JsonUtils.objectToJson(memberAccids));
        teamRequest.setMagree(0);
        teamRequest.setJoinmode(0);
        teamRequest.setBeinvitemode(1);
        teamRequest.setInvitemode(1);
        teamRequest.setUptinfomode(1);
        teamRequest.setUpcustommode(0);

        return teamRequest;
    }

    private TeamAddRequestDto toTeamAddRequestDto(Session session, List<SessionMember> members) {
        TeamAddRequestDto addMembersRequest = new TeamAddRequestDto();
        addMembersRequest.setTid(session.getGroupId());
        addMembersRequest.setOwner(formatAccid(new UserIdentifier(session.getUserId(), session.getUserType())));
        List<String> newMemberAccids = members.stream()
                .map(member -> formatAccid(new UserIdentifier(member.getUserId(), member.getUserType())))
                .collect(Collectors.toList());
        addMembersRequest.setMembers(JsonUtils.objectToJson(newMemberAccids));
        addMembersRequest.setMagree(0);
        addMembersRequest.setMsg("邀请您加入群:" + session.getName());

        return addMembersRequest;
    }

    /**
     * 群组会话的动作定义.
     */
    public enum GroupAction {

        CREATE("创建"), JOIN("加入"), QUIT("退出");

        private final String name;

        GroupAction(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static void main(String[] args) {
//        Tips tips = new NormalTips("这是测试的小灰条","111");
//        SystemNotice notice = new TipsMessageSystemNotice(tips);
//        System.out.println(notice.toString());
//        List<String> receivers = new ArrayList<>();
//        receivers.add("AAA");
//        receivers.add("BBB");
//        System.out.println(JsonUtils.objectToJson(receivers));
    }

}