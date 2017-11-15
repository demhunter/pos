/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * IM通知文本的模板类.
 *
 * @author wayne
 * @version 1.0, 2016/11/11
 */
@Component
public class NoticeTemplate {

    @Value("${session.created.notice.template}")
    private String sessionCreated;

    @Value("${session.rename.notice.template}")
    private String sessionRename;

    @Value("${session.firstMessage.title.template}")
    private String sessionFirstMsgTitle;

    @Value("${session.firstMessage.content.template}")
    private String sessionFirstMsgContent;

    @Value("${session.firstMessage.content.extend.template}")
    private String sessionFirstMsgContentExtend;

    @Value("${session.default.greeting}")
    private String sessionDefaultGreeting;

    @Value("${session.customer.default.languages}")
    private String sessionCustomerDefaultLanguages;

    @Value("${session.employee.default.languages}")
    private String sessionEmployeeDefaultLanguages;

    public String getSessionCreated() {
        return sessionCreated;
    }

    public String getSessionFirstMsgTitle() {
        return sessionFirstMsgTitle;
    }

    public String formatSessionRename(String... vars) {
        return String.format(sessionRename, vars);
    }

    public String formatSessionFirstMsgContent(String... vars) {
        return String.format(sessionFirstMsgContent, vars);
    }

    public String formatSessionFirstMsgContentExtend(String... vars) {
        return String.format(sessionFirstMsgContentExtend, vars);
    }

    public String formatSessionDefaultGreeting(String... vars) {
        return String.format(sessionDefaultGreeting, vars);
    }

    public String[] getSessionCustomerDefaultLanguages() {
        return sessionCustomerDefaultLanguages.split("\\|");
    }

    public String[] getSessionEmployeeDefaultLanguages() {
        return sessionEmployeeDefaultLanguages.split("\\|");
    }
}