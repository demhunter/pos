/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.console.controller.user;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.user.constant.UserType;
import com.pos.user.dto.login.IdentityInfoDto;
import com.pos.user.dto.login.UserExtensionInfoDto;
import com.pos.user.dto.login.LoginInfoDto;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.service.LoginService;
import com.pos.user.session.SessionUtils;
import com.pos.web.console.vo.user.UserForm;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 快捷收款管理员相关接口
 *
 * @author wangbing
 * @version 1.0, 2017/10/12
 */
@RestController
@RequestMapping("/user")
@Api(value = "user", description = "快捷收款管理员相关接口")
public class UserController {

    @Resource
    private SecurityService securityService;

    @Resource
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "管理员登录接口", notes = "管理员登录接口")
    public ApiResult<ManagerDto> login(
            @ApiParam(name = "userForm", value = "用户名,用户密码,验证码")
            @RequestBody UserForm userForm,
            HttpSession httpSession, HttpServletRequest request) {
        return doLogin(userForm, httpSession, request, true);
    }

    @RequestMapping(value = "/loginTest", method = RequestMethod.POST)
    @ApiOperation(value = "管理员登录测试接口", notes = "管理员登录测试接口，用于开发/测试环境下调试")
    public ApiResult<ManagerDto> loginTest(
            @ApiParam(name = "userForm", value = "用户名,用户密码,验证码")
            @RequestBody UserForm userForm,
            HttpSession httpSession, HttpServletRequest request) {
        return doLogin(userForm, httpSession, request, false);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiOperation(value = "管理员登出接口", notes = "管理员登出接口")
    public ApiResult<NullObject> logout(HttpSession httpSession) {
        SessionUtils.removeUserInfo(httpSession);
        return ApiResult.succ(null, "管理员已退出！");
    }

    @SuppressWarnings("all")
    private ApiResult<ManagerDto> doLogin(UserForm userForm, HttpSession httpSession, HttpServletRequest request, boolean encrypt) {
        String password;
        if (encrypt) {
            password = securityService.decryptData(userForm.getPassword());
        } else {
            password = userForm.getPassword();
        }

        LoginInfoDto loginInfo = new LoginInfoDto();

        IdentityInfoDto identity = new IdentityInfoDto();
        identity.setUserType(UserType.MANAGER.getValue());
        identity.setLoginName(userForm.getUserName());
        identity.setPassword(password);
        identity.setSmsCode(userForm.getSmsCode());

        UserExtensionInfoDto userExtensionInfo = new UserExtensionInfoDto();
        userExtensionInfo.setIp(HttpRequestUtils.getRealRemoteAddr(request));

        loginInfo.setIdentityInfoDto(identity);
        loginInfo.setUserExtensionInfo(userExtensionInfo);

        ApiResult<ManagerDto> apiResult = (ApiResult<ManagerDto>) loginService.login(loginInfo);

        if (apiResult.isSucc()) {
            SessionUtils.addUserInfo(httpSession, apiResult.getData());
        }
        return apiResult;
    }

}
