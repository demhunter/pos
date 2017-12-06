/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.fsm;

import com.pos.basic.sm.fsm.FSM;
import com.pos.common.util.validation.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthorityFSMFactory
 *
 * @author wangbing
 * @version 1.0, 2017/10/17
 */
public class AuthorityFSMFactory {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityFSMFactory.class);

    /**
     * 产生新的收款用户身份认证审核状态机实例
     *
     * @param currentState 当前状态String
     * @param context      用户身份认证审核Context
     * @return 用户身份认证审核状态机
     */
    public static FSM newAuditInstance(String currentState, Object context) {
        return generateInstance("fsm/sm_authority_audit.xml", currentState, context);
    }

    private static FSM generateInstance(String path, String currentState, Object context) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(path) && currentState != null && context != null, "参数不能为空！");

        try {
            FSM fsm = new FSM(AuthorityFSMFactory.class.getClassLoader().getResourceAsStream(path), context);
            fsm.setCurrentState(currentState);

            return fsm;
        } catch (Exception e) {
            logger.error("创建权限管理状态机失败", e);
            throw new IllegalStateException("创建权限管理状态机失败");
        }
    }
}
