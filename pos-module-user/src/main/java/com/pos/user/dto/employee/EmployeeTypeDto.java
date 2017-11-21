package com.pos.user.dto.employee;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.constant.EmployeeType;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/10/17
 */
public class EmployeeTypeDto implements Serializable {

    @ApiModelProperty("业者类型码")
    private Byte code;

    @ApiModelProperty("业者详细类型")
    private String desc;

    public EmployeeTypeDto(EmployeeType type){
        this.code = type.getCode();
        this.desc = type.getDesc();
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
