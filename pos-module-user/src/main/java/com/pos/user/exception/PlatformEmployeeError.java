/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.exception;

import com.pos.common.util.exception.ErrorCode;

/**
 * 平台业者错误码
 *
 * @author wangbing
 * @version 1.0, 2017/7/6
 */
public enum PlatformEmployeeError implements ErrorCode {

    PLATFORM_EMPLOYEE_NOT_EXISTED(1501, "家居顾问不存在"),

    PLATFORM_EMPLOYEE_ACCEPT_DISTRIBUTION_ONLY_ONE(1502, "当前是最后一个可接单的家居顾问，不允许变更为不可接单"),

    PLATFORM_EMPLOYEE_CUSTOMER_NOT_EXISTED(1503, "您与客户不存在关联"),

    PLATFORM_EMPLOYEE_CUSTOMER_ALTER_COMPLETEORDER(1504, "已成单状态中的客户不允许更新为其它状态"),

    PLATFORM_EMPLOYEE_MOBILE_IS_REGISTED(1505, "用户已存在！"),

    PLATFORM_EMPLOYEE_MOBILE_REGISTED_B_E(1506, "该手机号已注册为装修公司从业人员，不能开通平台家居顾问角色！"),

    PLATFORM_EMPLOYEE_MANAGER_IS_EXIST(1507, "此区域已存在客服经理"),

    PLATFORM_EMPLOYEE_CUSTOMER_TO_COMPLETEORDER(1508, "您不能把客户的状态更新为已成单"),

    PLATFORM_EMPLOYEE_CUSTOMER_CHANGE_PE_FAIL(1509, "更换失败，家居顾问未关闭会话太多，无法更换。"),

    PLATFORM_EMPLOYEE_FORBIDDING(1510, "您的账户已被平台管理员禁用，暂时无权访问！"),

    PLATFORM_EMPLOYEE_QUITED(1511, "您已离职，无权访问！"),

    PLATFORM_EMPLOYEE_NOT_CSM(1512, "您不是客服经理，无权访问！"),

    PLATFORM_DISABLE_CANNOT_DISTRIBUTION(1513, "已禁用的家居顾问不能设置为可接单！"),

    PLATFORM_EMPLOYEE_DISABLE(1514, "选择的家居顾问被禁用，不能更换"),

    PLATFORM_EMPLOYEE_TOP_LIMIT(1515, "家居顾问已达到服务上限不能修改接受派单状态"),

    PLATFORM_EMPLOYEE_NOT_IN_AREA(1516, "你选择更换的家居顾问与原来的不再同一服务区域"),

    TALKORDER_NOT_COMPLETEORDER_AND_FINISHORDER(1517, "谈单状态不能手动切换为成单和完结状态"),

    REFUSEORDER_NOT_COMPLETEORDER_AND_FINISHORDER(1518, "飞单状态不能手动切换为成单和完结状态"),

    COMPLETEORDER_NOT_TALKORDER_AND_REFUSEORDER(1519, "成单状态不能手动切换为谈单和飞单状态"),

    FINISHORDER_NOT_TALKORDER_AND_REFUSEORDER(1520, "完结状态不能手动切换为谈单和飞单状态");


    private final int code;

    private final String message;

    PlatformEmployeeError(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
