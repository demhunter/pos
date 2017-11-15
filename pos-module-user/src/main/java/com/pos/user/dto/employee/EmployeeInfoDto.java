package com.pos.user.dto.employee;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.user.constant.EmployeeType;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/10/13
 */
public class EmployeeInfoDto implements Serializable {

    @ApiModelProperty("员工Id")
    private Long userId;

    @ApiModelProperty("员工名")
    private String userName;

    @ApiModelProperty("用户细分类型(int)：1 = 设计师，2 = 项目经理，3 = 商务代表，10 = 家居顾问")
    private byte userDetailType;

    @ApiModelProperty("用户细分类型的值描述")
    private String userDetailTypeDesc;

    public EmployeeType parseUserDetailType() {
        return EmployeeType.getEnum(userDetailType);
    }

    public String getUserDetailTypeDesc() {
        if (!StringUtils.isEmpty(userDetailTypeDesc)) {
            return userDetailTypeDesc;
        }
        EmployeeType type = parseUserDetailType();
        return type != null ? type.getDesc() : "";
    }

    public void setUserDetailTypeDesc(String userDetailTypeDesc) {
        this.userDetailTypeDesc = userDetailTypeDesc;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte getUserDetailType() {
        return userDetailType;
    }

    public void setUserDetailType(byte userDetailType) {
        this.userDetailType = userDetailType;
    }
}
