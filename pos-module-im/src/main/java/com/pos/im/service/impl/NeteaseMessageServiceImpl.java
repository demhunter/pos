/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.pos.im.dao.NeteaseMessageDao;
import com.pos.im.domain.NeteaseMessage;
import com.pos.im.service.NeteaseMessageService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 睿智
 * @version 1.0, 2017/9/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NeteaseMessageServiceImpl implements NeteaseMessageService {

    @Resource
    private NeteaseMessageDao neteaseMessageDao;

    @Override
    public void saveMsg(String message) {

        JSONObject jsonObject = new JSONObject(message);
        String msgServerId = (String) jsonObject.get("msgidServer");
        Long msgId = neteaseMessageDao.findByMsgServerId(msgServerId);
        if(msgId==null||msgId==0){
            NeteaseMessage neteaseMessage = new NeteaseMessage();
            String eventType = jsonObject.optString("eventType");
            String convType = jsonObject.optString("convType");
            String to = jsonObject.optString("to");
            String fromAccount = jsonObject.optString("fromAccount");
            String msgTimestamp = jsonObject.optString("msgTimestamp");
            Date sendTime = new Date();
            if(StringUtils.isNotBlank(msgTimestamp)) {
                 sendTime = new Date(Long.parseLong(msgTimestamp));
            }
            String msgType = jsonObject.optString("msgType");
            String attach  = jsonObject.optString("attach");
            String body = jsonObject.optString("body");
            String msgClientId = jsonObject.optString("msgidClient");
            String customApnsText = jsonObject.optString("customApnsText");
            String tMembers = jsonObject.optString("tMembers");
            String ext = jsonObject.optString("ext");

            neteaseMessage.setAttach(attach);
            neteaseMessage.setBody(body);
            neteaseMessage.setConvType(convType);
            neteaseMessage.setCustomApnsText(customApnsText);
            neteaseMessage.setEventType(eventType);
            neteaseMessage.setExt(ext);
            neteaseMessage.setFromAccount(fromAccount);
            neteaseMessage.setMsgClientId(msgClientId);
            neteaseMessage.setMsgServerId(msgServerId);
            neteaseMessage.setMsgType(msgType);
            neteaseMessage.setSendTime(sendTime);
            neteaseMessage.settMembers(tMembers);
            neteaseMessage.setTo(to);

            neteaseMessageDao.addMsg(neteaseMessage);
        }

    }
}
