/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.session.build;

import com.pos.im.dto.user.UserInfoDto;
import com.pos.im.service.IMUserService;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 添加/移除会话成员的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/12/2
 */
@ApiModel
public class SessionMemberBuildDto implements Serializable {

    private static final long serialVersionUID = 3952942691756120561L;

    @ApiModelProperty("会话ID")
    private Long sessionId;

    @ApiModelProperty("成员ID（注意是用户在第三方IM平台的UID）")
    private String[] membersId;

    public Set<UserInfoDto> parseMembersId() {
        Set<UserInfoDto> members = new LinkedHashSet<>();
        for (String memberImUid : membersId) {
            members.add(IMUserService.parseIMUid(memberImUid));
        }
        return members;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String[] getMembersId() {
        return membersId;
    }

    public void setMembersId(String[] membersId) {
        this.membersId = membersId;
    }

}