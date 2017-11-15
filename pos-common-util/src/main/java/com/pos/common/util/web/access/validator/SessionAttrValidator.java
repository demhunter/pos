/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.web.access.validator;

import com.pos.common.util.web.access.PermissionValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 通过Session属性值进行访问验证的Validator.
 *
 * @author wayne
 * @version 1.0, 2016/6/29
 */
public class SessionAttrValidator implements PermissionValidator {

    /**
     * 键值名, 如果session中取不到该键值的attribute, 则不能通过验证
     */
    private String key;

    /**
     * 值类型, 可以为空, 如果设置了该值, 则attribute的值必须是该类型, 或者是其派生类时，才可以通过验证
     */
    private Class<?> type;

    @Override
    public boolean isValidated(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object attribute = session.getAttribute(key);
        if (attribute != null) {
            if (type != null) {
                return type.isAssignableFrom(attribute.getClass());
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

}