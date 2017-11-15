/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.im.condition.query.InternalMessageCondition;
import com.pos.im.dto.message.InternalMessageDto;
import com.pos.im.dto.message.SessionMessageDto;
import com.pos.im.condition.orderby.MessageOrderField;
import com.pos.im.dto.message.HistoryMessageDto;

import java.util.List;

/**
 * IM消息服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/10/24
 */
public interface IMMessageService {

    /**
     * 添加一个历史消息处理记录.
     *
     * @param dto
     * @return 添加成功返回true
     */
    boolean addHistoryMessage(HistoryMessageDto dto);

    /**
     * 更新指定历史消息处理记录.
     *
     * @param dto
     */
    void updateHistoryMessage(HistoryMessageDto dto);

    /**
     * 查询最近日期的历史消息处理记录.
     *
     * @return
     */
    HistoryMessageDto findLastHistoryMessage();

    /**
     * 查询指定状态的历史消息处理记录.
     *
     * @param status  指定的一组状态
     * @param maxSize 最多获取多少条记录
     * @return
     */
    List<HistoryMessageDto> findHistoryMessagesByStatus(byte[] status, int maxSize);

    /**
     * 添加一组会话消息.
     *
     * @param messages 消息列表
     */
    void addCaseSessionMessages(List<SessionMessageDto> messages);

    /**
     * 查询指定会话的消息记录.
     *
     * @param sessionId   会话ID
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link MessageOrderField}
     * @return
     */
    Pagination<SessionMessageDto> findCaseSessionMessages(
            Long sessionId, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 向指定用户组发送一条站内消息.
     *
     * @param messageDto 消息对象
     * @param receiverId 接收消息的用户ID, 注意是IMUid, 可以向多人发送（最多100个人）
     * @return 发送成功返回true, 失败返回false
     */
    boolean sendInternalMessageForBatch(InternalMessageDto messageDto, List<String> receiverId);

    /**
     * 向指定用户组发送一条站内消息.
     *
     * @param messageDto 消息对象
     * @param receiver   接收方用户标识
     * @return 发送成功返回true, 失败返回false
     */
    boolean sendInternalMessage(InternalMessageDto messageDto, UserIdentifier receiver);

    /**
     * 查询指定条件的站内消息记录.
     *
     * @param condition   查询条件
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link MessageOrderField}
     * @return
     */
    Pagination<InternalMessageDto> findInternalMessages(
            InternalMessageCondition condition, LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 获取指定站内消息对应内容的URL.
     *
     * @param messageId 消息ID
     * @return
     */
    ApiResult<String> getInternalMessageTargetURL(Long messageId);

}