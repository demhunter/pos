/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.customer.controller.user;

import com.pos.basic.service.SecurityService;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.user.constant.UserType;
import com.pos.user.dto.login.IdentityInfoDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.login.LoginInfoDto;
import com.pos.user.dto.login.RegisterInfoDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.LoginService;
import com.pos.user.service.RegisterService;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import com.pos.user.session.UserSessionPosComponent;
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
 * 基本用户相关Controller
 *
 * @author wangbing
 * @version 1.0, 2017/11/14
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", description = "v1.0.0 * 基本用户相关接口")
public class UserController {

    @Resource
    private RegisterService registerService;

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @Resource
    private SecurityService securityService;

    @Resource
    private UserSessionPosComponent userSessionPosComponent;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 wb 用户注册请求", notes = "v1.0.0 wb 用户注册请求(v1.0.0 * 关系绑定从登陆移到注册)")
    public ApiResult<CustomerDto> register(
            @ApiParam(name = "registerInfo", value = "注册相关信息")
            @RequestBody RegisterInfoDto registerInfo,
            HttpServletRequest request, HttpSession session) {
        // 解密密码
        String decryptedPassword = securityService.decryptData(registerInfo.getIdentityInfoDto().getPassword());

        registerInfo.getIdentityInfoDto().setPassword(decryptedPassword);
        registerInfo.getIdentityInfoDto().setUserType(UserType.CUSTOMER.getValue());
        registerInfo.getUserExtensionInfo().setIp(HttpRequestUtils.getRealRemoteAddr(request));
        ApiResult<CustomerDto> apiResult = registerService.addCustomer(registerInfo, true);

        if (!apiResult.isSucc()) {
            apiResult.getData().setUserSession(userSessionPosComponent.add(session, new UserInfo(apiResult.getData())));
            apiResult.setMessage("客户注册成功！");
        }

        return apiResult;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 wb 用户登录接口", notes = "用户登录接口(v1.0.0 * 关系绑定从登陆移到注册)")
    public ApiResult<CustomerDto> login(
            @ApiParam(name = "loginInfo", value = "登录相关信息")
            @RequestBody LoginInfoDto loginInfo,
            HttpServletRequest request, HttpSession session) {

        loginInfo.getUserExtensionInfo().setIp(HttpRequestUtils.getRealRemoteAddr(request));

        return doLogin(loginInfo, true, session);
    }

    @RequestMapping(value = "login-test", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * wb 登录测试接口", notes = "管理员登录测试接口，用于开发/测试环境下调试")
    public ApiResult<CustomerDto> loginTest(
            @ApiParam(name = "loginInfo", value = "登录相关信息")
            @RequestBody LoginInfoDto loginInfo,
            HttpServletRequest request, HttpSession session) {

        loginInfo.getUserExtensionInfo().setIp(HttpRequestUtils.getRealRemoteAddr(request));

        return doLogin(loginInfo, false, session);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ApiOperation(value = "用户登出接口", notes = "用户登出接口")
    public ApiResult<NullObject> logout(@FromSession UserInfo userInfo, HttpSession httpSession) {
        if (userInfo == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_LOGIN);
        }
        userSessionPosComponent.remove(httpSession, userInfo);
        return ApiResult.succ(null, "用户已退出！");
    }

    @RequestMapping(value = "password", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * wb 找回密码请求", notes = "找回密码请求，找回成功后则自动登录")
    public ApiResult<CustomerDto> getBackPassword(
            @ApiParam(name = "loginInfo", value = "登录相关信息")
            @RequestBody LoginInfoDto loginInfo,
            HttpServletRequest request, HttpSession session) {
        IdentityInfoDto identityInfoDto = loginInfo.getIdentityInfoDto();
        // 解密密码
        String decryptedPassword = securityService.decryptData(identityInfoDto.getPassword());
        ApiResult passwordResult = userService.updatePwdByPhone(identityInfoDto.getLoginName(),
                decryptedPassword, identityInfoDto.getSmsCode(), UserType.CUSTOMER);

        if (passwordResult.isSucc()) {
            // 找回密码成功，执行登录操作
            loginInfo.getUserExtensionInfo().setIp(HttpRequestUtils.getRealRemoteAddr(request));
            return doLogin(loginInfo, true, session);
        } else {
            return ApiResult.fail(passwordResult.getError());
        }
    }


    /**
     * 登录处理
     *
     * @param loginInfo 登录相关信息
     * @param encrypt   是否进行解密操作
     * @param session   HttpSession
     * @return 登录结果
     */
    private ApiResult<CustomerDto> doLogin(LoginInfoDto loginInfo, boolean encrypt, HttpSession session) {
        // 解密密码
        String password;
        if (encrypt) {
            password = securityService.decryptData(loginInfo.getIdentityInfoDto().getPassword());
        } else {
            password = loginInfo.getIdentityInfoDto().getPassword();
        }
        // 设置登录信息
        loginInfo.getIdentityInfoDto().setPassword(password);
        loginInfo.getIdentityInfoDto().setUserType(UserType.CUSTOMER.getValue());

        ApiResult<CustomerDto> apiResult = (ApiResult<CustomerDto>) loginService.login(loginInfo);

        if (apiResult.isSucc()) {
            apiResult.getData().setUserSession(userSessionPosComponent.add(session, new UserInfo(apiResult.getData())));
        }

        return apiResult;
    }

}
