/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.constant.InternalMessageType;
import com.pos.im.dao.InternalMessageDao;
import com.pos.im.dao.SessionMessageDao;
import com.pos.im.domain.InternalMessage;
import com.pos.im.dto.message.SessionMessageDto;
import com.pos.im.service.IMUserService;
import com.pos.im.service.support.IMClient;
import com.pos.im.condition.orderby.MessageOrderField;
import com.pos.im.condition.query.InternalMessageCondition;
import com.pos.im.dao.HistoryMessageDao;
import com.pos.im.domain.HistoryMessage;
import com.pos.im.domain.InternalMessageReceiver;
import com.pos.im.dto.message.HistoryMessageDto;
import com.pos.im.dto.message.InternalMessageDto;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.service.IMMessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IM消息服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/10/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IMMessageServiceImpl implements IMMessageService {

    private Logger logger = LoggerFactory.getLogger(IMMessageServiceImpl.class);

    @Resource
    private IMClient apiClientConfig;

    @Resource
    private HistoryMessageDao historyMessageDao;

    @Resource
    private SessionMessageDao sessionMessageDao;

    @Resource
    private InternalMessageDao internalMessageDao;

    @Value("${app.base.url}")
    private String baseUrl;

    /**
     * 单条站内消息最多只能推送给多少个用户(ps: 融云上限为100)
     */
    private final int internalMessageMaxReceivers = 100;

    @Override
    public boolean addHistoryMessage(HistoryMessageDto dto) {
        if (historyMessageDao.findByHistDate(dto.getHistDate()) == null) {
            historyMessageDao.save(convert2Entity(dto));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateHistoryMessage(HistoryMessageDto dto) {
        historyMessageDao.update(convert2Entity(dto));
    }

    @Override
    public HistoryMessageDto findLastHistoryMessage() {
        return convert2Dto(historyMessageDao.findLast());
    }

    @Override
    public List<HistoryMessageDto> findHistoryMessagesByStatus(byte[] status, int maxSize) {
        List<HistoryMessage> list = historyMessageDao.findListByStatus(status, maxSize);
        return list != null && !list.isEmpty() ? list.stream()
                .map(entity -> convert2Dto(entity)).collect(Collectors.toList()) : null;
    }

    @Override
    public void addCaseSessionMessages(List<SessionMessageDto> messages) {
        sessionMessageDao.saveBatch(messages);
    }

    @Override
    public Pagination<SessionMessageDto> findCaseSessionMessages(
            Long sessionId, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(MessageOrderField.getInterface());
        }

        int count = sessionMessageDao.countBySessionId(sessionId);
        Pagination<SessionMessageDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<SessionMessageDto> list = sessionMessageDao
                    .findListBySessionId(sessionId, limitHelper, orderHelper);
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public boolean sendInternalMessageForBatch(InternalMessageDto messageDto, List<String> receiverId) {
        FieldChecker.checkEmpty(messageDto, "messageDto");
        FieldChecker.checkEmpty(receiverId, "receiverId");
        messageDto.check("messageDto");
        if (receiverId.size() > internalMessageMaxReceivers) {
            throw new IllegalParamException("单条站内消息最多只能推送给"
                    + internalMessageMaxReceivers + "个用户！receivers: " + receiverId.size());
        }

        // 检查并解析接收者的用户信息
        List<UserInfoDto> receiveUsers = new ArrayList<>(receiverId.size());
        receiverId.forEach(imUid -> receiveUsers.add(IMUserService.parseIMUid(imUid)));

        // 先持久化消息后再向用户推送
        InternalMessage message = convert2Entity(messageDto);
        internalMessageDao.save(message);
        messageDto.setId(message.getId());
        List<InternalMessageReceiver> receivers = new ArrayList<>(receiverId.size());
        receiveUsers.forEach(receiveUser -> {
            InternalMessageReceiver receiver = new InternalMessageReceiver();
            receiver.setMsgId(message.getId());
            receiver.setUserId(receiveUser.getUserId());
            receiver.setUserType(receiveUser.getUserType());
            receivers.add(receiver);
        });
        internalMessageDao.saveReceivers(receivers);

        return apiClientConfig.sendInternalMessageForBatch(messageDto,
                receiveUsers.stream().map(UserInfoDto::buildUserIdentifier).collect(Collectors.toList()));
        /*try {
            doSendInternalMessage(messageDto, receiverId);
            return true;
        } catch (IMServerException e) {
            logger.error("发送站内消息失败！messageDto = " + messageDto, e);
            return false;
        }*/
    }

    @Override
    public boolean sendInternalMessage(InternalMessageDto messageDto, UserIdentifier receiver) {
        FieldChecker.checkEmpty(messageDto, "messageDto");
        FieldChecker.checkEmpty(receiver, "receiver");
        messageDto.check("messageDto");

        // 先持久化消息后再向用户推送
        InternalMessage message = convert2Entity(messageDto);
        internalMessageDao.save(message);
        messageDto.setId(message.getId());
        InternalMessageReceiver messageReceiver = new InternalMessageReceiver();
        messageReceiver.setMsgId(message.getId());
        messageReceiver.setUserId(receiver.getUserId());
        messageReceiver.setUserType(receiver.getUserType());
        internalMessageDao.saveReceiver(messageReceiver);

        return apiClientConfig.sendInternalMessage(messageDto, receiver);
    }

    @Override
    public Pagination<InternalMessageDto> findInternalMessages(
            InternalMessageCondition condition, LimitHelper limitHelper, OrderHelper orderHelper) {
        FieldChecker.checkEmpty(condition, "condition");
        FieldChecker.checkEmpty(limitHelper, "limitHelper");
        if (orderHelper != null) {
            orderHelper.validate(MessageOrderField.getInterface());
        }

        int count = internalMessageDao.getTotalByCondition(condition);
        Pagination<InternalMessageDto> pagination = Pagination.newInstance(limitHelper, count);
        if (count > 0) {
            List<InternalMessageDto> list = internalMessageDao.findByCondition(condition, limitHelper, orderHelper);
            pagination.setResult(list);
        }

        return pagination;
    }

    @Override
    public ApiResult<String> getInternalMessageTargetURL(Long messageId) {
        FieldChecker.checkEmpty(messageId, "messageId");
        InternalMessageDto msg = internalMessageDao.get(messageId);
        if (msg == null) {
            return ApiResult.fail(CommonErrorCode.DATA_NOT_FOUND);
        }
        if (!InternalMessageType.TWITTER.compare((byte) msg.getType()) && !InternalMessageType.ORDER.compare((byte)msg.getType())) {
            return ApiResult.fail(CommonErrorCode.UNSUPPORTED_OPERATION);
        }

        InternalMessageType.SubType subType = msg.parseSubType();
        if (subType != null) {
            String url;
            if (msg.getTargetId() == null || msg.getTargetId() != 0L) {
                url = baseUrl + String.format(subType.getURI(), msg.getTargetId());
            } else {
                url = baseUrl + String.format(subType.getURI(), StringUtils.split(msg.getParameters(), ','));
            }

            return ApiResult.succ(url);
        } else {
            return ApiResult.fail(CommonErrorCode.URL_ERROR);
        }
    }


    @SuppressWarnings("all")
    private HistoryMessageDto convert2Dto(HistoryMessage entity) {
        if (entity != null) {
            HistoryMessageDto dto = new HistoryMessageDto();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } else {
            return null;
        }
    }

    @SuppressWarnings("all")
    private HistoryMessage convert2Entity(HistoryMessageDto dto) {
        if (dto != null) {
            HistoryMessage entity = new HistoryMessage();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } else {
            return null;
        }
    }

    @SuppressWarnings("all")
    private InternalMessageDto convert2Dto(InternalMessage entity) {
        if (entity != null) {
            InternalMessageDto dto = new InternalMessageDto();
            BeanUtils.copyProperties(entity, dto);
            dto.setType(entity.getType());
            return dto;
        } else {
            return null;
        }
    }

    @SuppressWarnings("all")
    private InternalMessage convert2Entity(InternalMessageDto dto) {
        if (dto != null) {
            InternalMessage entity = new InternalMessage();
            BeanUtils.copyProperties(dto, entity);
            entity.setType((byte) dto.getType());
            return entity;
        } else {
            return null;
        }
    }

}