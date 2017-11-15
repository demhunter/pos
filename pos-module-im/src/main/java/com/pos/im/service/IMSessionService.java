/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.im.condition.orderby.SessionOrderField;
import com.pos.im.condition.query.SessionClosedCondition;
import com.pos.im.condition.query.SessionCommonCondition;
import com.pos.im.condition.query.SessionConsoleCondition;
import com.pos.im.dto.session.SessionConsoleListDto;
import com.pos.im.dto.session.SessionCreateReturnDto;
import com.pos.im.dto.session.SessionDto;
import com.pos.im.constant.SessionClosedReason;
import com.pos.im.constant.UserJoinSessionType;
import com.pos.im.dto.session.SessionMemberDto;
import com.pos.im.dto.session.build.SessionBuildDto;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.exception.IMServerException;

import java.util.List;
import java.util.Set;

/**
 * IM会话服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/7/11
 */
public interface IMSessionService {

    int SESSION_NAME_MIN_LENGTH = 4;

    int SESSION_NAME_MAX_LENGTH = 50;

    String DEFAULT_GROUP_IMAGE = "http://o8nljewkg.bkt.clouddn.com/o_1ao3jmbhk11qj1q031k9s10685qac.png?width=120&height=120";

    /**
     * 创建一个会话.
     *
     * @param buildDto 会话创建信息
     * @return
     * @throws IMServerException 会话创建通过但向IM Server申请创建群组失败
     */
    ApiResult<SessionCreateReturnDto> create(SessionBuildDto buildDto) throws IMServerException;

    /**
     * 发送会话创建成功通知消息
     *
     * @param sessionReturn 会话创建成功后的返回信息
     */
    void sendSessionCreateMsg(SessionCreateReturnDto sessionReturn);

    /**
     * 为指定会话添加成员.
     * <p>
     * 可以加入业者(E)和业主(C)，客服加入会话应该调用addServant.
     *
     * @param sessionId  会话ID
     * @param membersId  成员ID列表
     * @param memberType 成员类型
     * @param operator   操作人标识
     * @return 成功加入的成员数量
     * @throws IMServerException 加入会话通过但向IM Server申请加入群组失败
     */
    ApiResult<Integer> addMembers(Long sessionId, Set<Long> membersId, String memberType,
                                  UserIdentifier operator) throws IMServerException;

    /**
     * 移除指定会话的成员(不包括客服).
     * <p>
     * 会话创建者应该调用close来关闭会话，客服应该调用removeServant来退出会话.
     *
     * @param sessionId 会话ID
     * @param member    成员标识
     * @param operator  操作人标识(如果不为空，表示成员是被该用户踢出会话)
     * @throws IMServerException 移除指定会话的成员通过但向IM Server申请退出群组失败
     */
    ApiResult<NullObject> removeMember(Long sessionId, UserIdentifier member,
                                       UserIdentifier operator) throws IMServerException;

    /**
     * 退出指定会话
     *
     * @param sessionId 会话id
     * @param operator  操作人标识
     * @return 操作结果
     * @throws IMServerException 退出会话通过但向IM Server申请退出群组失败
     */
    ApiResult<NullObject> quitGroup(Long sessionId, UserIdentifier operator) throws IMServerException;

    /**
     * 为指定会话添加客服.
     *
     * @param sessionId 会话ID
     * @param servantId 客服ID
     * @throws IMServerException 加入会话通过但向IM Server申请加入群组失败
     */
    ApiResult<NullObject> addServant(Long sessionId, Long servantId) throws IMServerException;

    /**
     * 移除指定会话的客服.
     *
     * @param sessionId 会话ID
     * @param servantId 客服ID
     * @throws IMServerException 退出会话通过但向IM Server申请退出群组失败
     */
    ApiResult<NullObject> removeServant(Long sessionId, Long servantId) throws IMServerException;

    /**
     * 关闭指定会话.
     *
     * @param sessionId 会话ID
     * @param operator  操作人标识
     * @throws IMServerException 会话关闭通过但向IM Server申请解散群组失败
     */
    ApiResult<NullObject> close(Long sessionId, UserIdentifier operator) throws IMServerException;

    /**
     * 解散指定会话.
     * <p>
     * 与close的区别在于: 调用该方法表示由系统执行关闭操作, 而不是会话创建者或平台管理员
     *
     * @param sessionId    会话ID
     * @param closedReason 关闭原因
     * @throws IMServerException 会话关闭通过但向IM Server申请解散群组失败
     */
    ApiResult<NullObject> dismiss(
            Long sessionId, SessionClosedReason closedReason) throws IMServerException;

    /**
     * 修改指定会话的名称.
     *
     * @param sessionId      会话ID
     * @param newSessionName 修改后的会话名称
     * @param operator       操作人信息
     * @return 修改成功返回true
     * @throws IMServerException 会话名称修改成功但向IM Server刷新群组信息失败
     */
    ApiResult<Boolean> rename(
            Long sessionId, String newSessionName, UserInfoDto operator) throws IMServerException;

    /**
     * 累加会话的呼叫次数.
     *
     * @param sessionId  会话ID
     * @param employeeId 被呼叫的业者ID
     * @return 更新是否成功
     */
    boolean incrementCallTotal(Long sessionId, Long employeeId);

    /**
     * 标记会话第一条消息的发送者.
     * <p>
     * ps：每个会话只能被标记一次，被标记后会话不会被当作临时会话清理；<br>
     * 如果第一条消息的发送者即创建者，则向该会话的所有业者发送一条提醒短信和站内信(前提是业者所属公司启用了咨询提醒功能).
     *
     * @param groupId 会话ID
     * @param sender    发送者标识
     * @return 初次标记且标记成功则返回true
     */
    ApiResult<Boolean> markFirstMsgSender(String groupId, UserIdentifier sender);

    /**
     * 标记会话内容有风险或者正常.
     *
     * @param sessionId 会话ID
     * @param operator  操作人标识
     * @param warning   会话内容是否有风险 0, "正常" 1, "重要"  2, "风险"
     */
    ApiResult<NullObject> markWarningOrNormal(Long sessionId, UserIdentifier operator, byte warning);

    /**
     * 获取指定用户发起的针对指定案例的有效会话的群组id.
     *
     * @param user   用户标识
     * @param caseId 案例ID
     * @return
     */
    Long getSessionIdByUserCase(UserIdentifier user, Long caseId);

    /**
     * 获取指定用户发起的针对指定案例的有效会话的群组id.
     *
     * @param user   用户标识
     * @param caseId 案例ID
     * @return 群组groupId
     */
    String getSessionGroupIdByUserCase(UserIdentifier user, Long caseId);

    /**
     * 查询指定会话的成员标识.
     *
     * @param sessionId 会话ID
     * @return 只返回当前在会话中的成员
     */
    List<UserIdentifier> findMembersIdentifier(Long sessionId);

    /**
     * 查询指定会话的成员列表.
     *
     * @param sessionId    会话ID
     * @param queryCompany 是否查询成员所属的公司信息(针对B端和业者)
     * @return 只返回当前在会话中的成员
     */
    List<SessionMemberDto> findMembers(Long sessionId, boolean queryCompany);

    /**
     * 查询会话信息.
     *
     * @param sessionId 会话ID
     * @param condition 公共查询条件
     * @return
     */
    SessionDto findSession(Long sessionId, SessionCommonCondition condition);

    /**
     * 根据群组id查询会话信息.
     *
     * @param groupId   会话ID
     * @param condition 公共查询条件
     * @return 会话信息
     */
    SessionDto findSessionByGroupId(String groupId, SessionCommonCondition condition);

    /**
     * 查询符合条件的会话列表.
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link SessionOrderField}
     * @return
     */
    Pagination<SessionDto> querySessions(
            SessionCommonCondition condition, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询已关闭的会话列表.
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link SessionOrderField}
     * @return
     */
    Pagination<SessionDto> findSessionClosedList(
            SessionClosedCondition condition, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询指定用户发起的会话列表.
     *
     * @param user        用户标识
     * @param companyId   会话所属的公司ID(可空)
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link SessionOrderField}
     * @return
     */
    Pagination<SessionDto> findSessionListByUser(UserIdentifier user, Long companyId,
                                                 SessionCommonCondition condition,
                                                 LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询指定成员参与的会话列表.
     *
     * @param member      成员标识
     * @param joinType    成员加入类型(可空)
     * @param condition   公共查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link SessionOrderField}
     * @return
     */
    Pagination<SessionDto> findSessionListByMember(UserIdentifier member,
                                                   UserJoinSessionType joinType,
                                                   SessionCommonCondition condition,
                                                   LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询没有会话跟踪者(客服参与)的会话列表.
     *
     * @param warning     true表示只查询有风险的会话, false表示只查询无风险的会话, null表示查询所有会话
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link SessionOrderField}
     * @return
     */
    Pagination<SessionDto> findListNotServant(
            Boolean warning, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询临时会话的ID.
     *
     * @return
     */
    List<Long> findTempSessionIds();

    /**
     * 查询不活跃的会话ID.
     *
     * @return
     */
    List<Long> findInactiveSessionIds();

    /**
     * 获取用户加入了的会话id列表
     *
     * @param user      用户标识
     * @param available 是否退出会话，可空
     * @return 会话id列表
     */
    List<Long> querySessionIdsByUser(UserIdentifier user, Boolean available);

    /**
     * 查询负责指定公司案例的平台业者（即家居顾问）.
     *
     * @param caseId    案例ID
     * @param companyId 案例所属的公司ID
     * @return
     */
    UserInfoDto findPlatformEmployee(Long caseId, Long companyId);

    /**
     * v1.6.0 * 发送订单账单支付成功的小灰条信息
     *
     * @param sessionId  需要发往的目标
     * @param sendUser   消息发送人
     * @param msgContent 小灰条内容
     * @return
     */
    Boolean pushOrderBillPaySuccessNotice(Long sessionId, UserIdentifier sendUser, String msgContent);


    /**
     * M端查找会话
     * @param condition
     * @param limitHelper
     * @param orderHelper
     * @return
     */
    Pagination<SessionConsoleListDto> consoleQuerySession(SessionConsoleCondition condition, LimitHelper limitHelper, OrderHelper orderHelper, Long userId);

    /**
     * 获取群相关信息
     *
     * @param sessionId
     * @return
     */
    ApiResult<SessionConsoleListDto> querySessionInfo(Long sessionId, Long userId);
}
