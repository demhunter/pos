/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session.build;

import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.service.IMUserService;
import com.pos.im.dto.session.SessionMemberDto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 会话创建所需信息的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/11/24
 */
public abstract class SessionBuildDto implements Serializable {

    private static final long serialVersionUID = -2842708551755519183L;

    private UserInfoDto creator; // 会话发起人信息

    private Set<SessionMemberDto> members; // 加入会话的成员列表(不包括会话发起者)

    private String exclusiveTwitter; // 如果创建会话时客户有关联推客，且该推客是会话关联公司的业者，则记录专属推客的基本信息：userId,userType,name

    private UserInfoDto platformEmployee; // 客户关联家居顾问信息

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationException 未设置不能为空的字段, 或者字段值不符合约定
     */
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(creator, fieldPrefix + "creator");
        FieldChecker.checkEmpty(members, fieldPrefix + "members");
        creator.check(fieldPrefix + "creator");
        for (SessionMemberDto member : members) {
            member.check(fieldPrefix + "members");
            if (!IMUserService.USER_TYPE_EMPLOYEE.equals(member.getUserType())) {
                throw new ValidationException("创建会话时的成员只能是业者！");
            }
        }
    }

    /**
     * 获取加入会话的成员所属的公司ID(将去除重复的公司ID，即多个成员属于同一公司的情况).
     *
     * @return
     */
    public List<Long> distinctCompanyIdOfMembers() {
        return members.stream()
                .map(member -> member.getCompanyId())
                .distinct().collect(Collectors.toList());
    }

    /**
     * 获取会话名称.
     *
     * @return
     */
    public abstract String getSessionName();

    public void setExclusiveTwitter(Long userId, String userType, String name) {
        FieldChecker.checkEmpty(userId, "exclusiveTwitter.userId");
        FieldChecker.checkEmpty(userType, "exclusiveTwitter.userType");
        FieldChecker.checkEmpty(name, "exclusiveTwitter.name");
        exclusiveTwitter = userId + "," + userType + "," + name;
    }

    public final UserInfoDto getCreator() {
        return creator;
    }

    public final void setCreator(UserInfoDto creator) {
        this.creator = creator;
    }

    public final Set<SessionMemberDto> getMembers() {
        return members;
    }

    public final void setMembers(Set<SessionMemberDto> members) {
        this.members = members;
    }

    public String getExclusiveTwitter() {
        return exclusiveTwitter;
    }

    public UserInfoDto getPlatformEmployee() {
        return platformEmployee;
    }

    public void setPlatformEmployee(UserInfoDto platformEmployee) {
        this.platformEmployee = platformEmployee;
    }
}