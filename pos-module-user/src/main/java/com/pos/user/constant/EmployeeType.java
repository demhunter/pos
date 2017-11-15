/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.user.dto.employee.EmployeeTypeDto;

import java.util.ArrayList;
import java.util.List;

/**
 * B端从业者类型定义.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
public enum EmployeeType implements UserDetailType, CommonByteEnum {

    DESIGNER((byte) 1, "主创设计"), PM((byte) 2, "项目经理"), HOME_ADVISOR((byte) 3, "商务代表"),
    PLATFORM_BD((byte) 10, "家居顾问");

    private final byte code;

    private final String desc;

    EmployeeType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Override
    public byte getValue() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean compare(byte code) {
        return this.code == code;
    }

    public static EmployeeType getEnum(byte code) {
        for (EmployeeType type : EmployeeType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalParamException("非法的value值");
    }

    public static EmployeeType getEnum(String desc) {
        for (EmployeeType type : EmployeeType.values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }

    public static List<EmployeeTypeDto> getEmployeeUserType(){
        List<EmployeeTypeDto> employeeTypeList = new ArrayList<>();
        for (EmployeeType type : EmployeeType.values()){
            if (!type.equals(PLATFORM_BD)){
                employeeTypeList.add(new EmployeeTypeDto(type));
            }
        }
        return employeeTypeList;
    }
}
