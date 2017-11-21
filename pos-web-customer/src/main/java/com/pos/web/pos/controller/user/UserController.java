/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.web.pos.controller.user;

import com.pos.web.pos.vo.request.LoginRequestDto;
import com.pos.web.pos.vo.request.RegisterRequestDto;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.pos.basic.service.SecurityService;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.mvc.resolver.FromSession;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.web.http.HttpRequestUtils;
import com.pos.pos.dto.twitter.ReferrerSimpleDto;
import com.pos.pos.service.PosService;
import com.pos.pos.service.PosUserChannelInfoService;
import com.pos.user.constant.CustomerType;
import com.pos.user.constant.UserType;
import com.pos.user.dto.IdentityInfoDto;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.UserLoginDto;
import com.pos.user.dto.UserRegConfirmDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.CustomerService;
import com.pos.user.service.LoginService;
import com.pos.user.service.RegisterService;
import com.pos.user.service.UserService;
import com.pos.user.session.UserInfo;
import com.pos.user.session.UserSessionPosComponent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 基本用户相关Controller
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", description = "v1.0.0 * 基本用户相关接口(* 获取推荐人信息接口)")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private GlobalConstants globalConstants;

    @Resource
    private RegisterService registerService;

    @Resource
    private CustomerService customerService;

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @Resource
    private SmsService smsService;

    @Resource
    private SecurityService securityService;

    @Resource
    private UserSessionPosComponent userSessionPosComponent;

    @Resource
    private PosService posService;

    @Resource
    private PosUserChannelInfoService posUserChannelInfoService;

    @RequestMapping(value = "referrer/{referrerUserId}", method = RequestMethod.GET)
    @ApiOperation(value = "v1.0.0 * 分享出去的页面注册时获取推荐人信息", notes = "分享出去的页面注册时获取推荐人信息")
    public ApiResult<ReferrerSimpleDto> getReferrerInfo(
            @ApiParam(name = "referrerUserId", value = "推荐人userId")
            @PathVariable("referrerUserId") Long referrerUserId) {
        return posUserChannelInfoService.findReferrerSimpleInfo(referrerUserId);
    }

    /**
     * 用户注册请求
     *
     * @param loginInfo 注册相关信息
     * @return 注册结果
     */
    @SuppressWarnings("all")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 用户注册请求", notes = "用户注册请求")
    public ApiResult<UserRegConfirmDto> register(
            @ApiParam(name = "registerRequestDto", value = "注册相关信息")
            @RequestBody RegisterRequestDto registerRequestDto,
            HttpServletRequest request, HttpSession session) {
        // 解密密码
        String decryptedPassword = securityService.decryptData(registerRequestDto.getPassword());

        LoginInfoDto loginInfoDto = new LoginInfoDto();
        IdentityInfoDto identityInfoDto = new IdentityInfoDto();
        identityInfoDto.setLoginName(registerRequestDto.getPhone());
        identityInfoDto.setSmsCode(registerRequestDto.getSmsCode());
        loginInfoDto.setIdentityInfoDto(identityInfoDto);
        loginInfoDto.getIdentityInfoDto().setPassword(decryptedPassword);
        loginInfoDto.setIp(HttpRequestUtils.getRealRemoteAddr(request));
        loginInfoDto.setRecommendId(registerRequestDto.getLeaderId());
        loginInfoDto.setRecommendType(registerRequestDto.getType());
        ApiResult<UserRegConfirmDto> apiResult = registerService.addCustomer(loginInfoDto, true, CustomerType.NATURE);

        if (!apiResult.isSucc() || (apiResult.getData() != null && (apiResult.getData()).getNeedConfirm())) {
            return apiResult;
        }
        CustomerDto customerDto = customerService.findByUserPhone(registerRequestDto.getPhone(), false, false);
        customerDto.setUserSession(userSessionPosComponent.add(session, new UserInfo(customerDto)));
        /*// 绑定推客与注册用户的关系
        posService.posLogin(customerDto, registerRequestDto.getType(), registerRequestDto.getLeaderId());*/
        apiResult.getData().setCustomerDto(customerDto);
        apiResult.setMessage("客户注册成功！");
        return apiResult;
    }

    /**
     * 用户确认开通角色
     *
     * @param registerRequestDto 注册相关信息
     * @return 开通结果
     */
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 用户确认开通角色", notes = "用户确认开通角色")
    public ApiResult<CustomerDto> confirm(
            @ApiParam(name = "registerRequestDto", value = "登录相关信息")
            @RequestBody RegisterRequestDto registerRequestDto,
            HttpServletRequest request, HttpSession session) {
        LoginInfoDto loginInfoDto = new LoginInfoDto();
        IdentityInfoDto identityInfoDto = new IdentityInfoDto();
        identityInfoDto.setLoginName(registerRequestDto.getPhone());
        identityInfoDto.setSmsCode(registerRequestDto.getSmsCode());
        loginInfoDto.setIdentityInfoDto(identityInfoDto);
        loginInfoDto.setIp(HttpRequestUtils.getRealRemoteAddr(request));

        loginInfoDto.setRecommendId(registerRequestDto.getLeaderId());
        loginInfoDto.setRecommendType(registerRequestDto.getType());
        ApiResult apiResult = registerService.confirmCustomerRegister(loginInfoDto, true, CustomerType.NATURE);

        if (!apiResult.isSucc()) {
            return apiResult;
        }

        CustomerDto customerDto = customerService.findByUserPhone(registerRequestDto.getPhone(), false, false);
        customerDto.setUserSession(userSessionPosComponent.add(session, new UserInfo(customerDto)));

        return ApiResult.succ(customerDto, "客户注册成功！");
    }

    /**
     * 用户登录接口
     *
     * @param loginRequestDto 登录相关信息
     * @return 登录结果
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 用户登录接口", notes = "用户登录接口(wb * 只返回用户基本信息，其它信息再调用获取用户信息接口获取详细信息)")
    public ApiResult<CustomerDto> login(
            @ApiParam(name = "loginInfoVo", value = "登录相关信息")
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request, HttpSession session) {
        return doLogin(loginRequestDto, request, true, session);
    }

    /**
     * 用户登录测试接口
     *
     * @param loginRequestDto 登录相关信息
     * @return 登录结果
     */
    @RequestMapping(value = "loginTest", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 登录测试接口", notes = "管理员登录测试接口，用于开发/测试环境下调试")
    public ApiResult<CustomerDto> loginTest(
            @ApiParam(name = "loginInfoVo", value = "登录相关信息")
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request, HttpSession session) {
        return doLogin(loginRequestDto, request, false, session);
    }

    /**
     * 用户登出接口
     *
     * @return 登出结果
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 用户登出接口", notes = "用户登出接口")
    public ApiResult<NullObject> logout(@FromSession UserInfo userInfo, HttpSession httpSession) {
        if (userInfo == null) {
            return ApiResult.fail(UserErrorCode.USER_NOT_LOGIN);
        }
        userSessionPosComponent.remove(httpSession, userInfo);
        return ApiResult.succ(null, "用户已退出！");
    }

    /**
     * 发送短信验证码请求
     *
     * @param phone  用户手机号
     * @param source 请求来源 {@link MemcachedPrefixType}
     * @return 发送结果
     */
    @RequestMapping(value = "sendSmsCode", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 发送短信验证码请求", notes = "发送短信验证码请求")
    public ApiResult sendSmsCode(
            @ApiParam(name = "phone", value = "用户手机号")
            @RequestParam("phone") String phone,
            @ApiParam(name = "source", value = "请求来源：1 = 用户注册，2 = 找回密码，3 = 登录验证，8 = 公司入驻申请，9 = 推客报备客户, 10 = 邀请成为推客, 11 = 邀请客户享受主材补贴")
            @RequestParam("source") int source) {
        ApiResult apiResult = smsService.sendVerifyCode(phone, MemcachedPrefixType.getEnum((byte) source));
        if (!apiResult.isSucc()) {
            apiResult.setMessage("验证码发送失败");
        }

        return apiResult;
    }

    /**
     * 找回密码请求
     *
     * @param loginInfoVo 登录相关信息
     * @return 用户信息结果
     */
    @RequestMapping(value = "getBackPassword", method = RequestMethod.POST)
    @ApiOperation(value = "v1.0.0 * 找回密码请求", notes = "找回密码请求，找回成功后如果用户已经开通C端账号则自动登录(v1.3.3 返回自定义UserSession)")
    public ApiResult getBackPassword(
            @ApiParam(name = "loginInfoVo", value = "登录相关信息")
            @RequestBody RegisterRequestDto loginInfoVo) {
        // 解密密码
        String decryptedPassword = securityService.decryptData(loginInfoVo.getPassword());
        return userService.updatePwdByPhone(loginInfoVo.getPhone(),
                decryptedPassword, loginInfoVo.getSmsCode(), UserType.CUSTOMER);
    }


    /**
     * 登录处理
     *
     * @param loginRequestDto 登录相关信息
     * @param encrypt         是否进行解密操作
     * @return 登录结果
     */
    private ApiResult<CustomerDto> doLogin(LoginRequestDto loginRequestDto, HttpServletRequest request, boolean encrypt, HttpSession session) {
        // 解密密码
        String password;
        if (encrypt) {
            password = securityService.decryptData(loginRequestDto.getPassword());
        } else {
            password = loginRequestDto.getPassword();
        }

        LoginInfoDto loginInfoDto = new LoginInfoDto();
        IdentityInfoDto identityInfoDto = new IdentityInfoDto();
        identityInfoDto.setLoginName(loginRequestDto.getPhone());
        identityInfoDto.setPassword(password);
        loginInfoDto.setIdentityInfoDto(identityInfoDto);
        loginInfoDto.setIp(HttpRequestUtils.getRealRemoteAddr(request));

        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setLoginName(identityInfoDto.getLoginName());
        userLoginDto.setPassword(password);
        userLoginDto.setUserType(UserType.CUSTOMER);
        ApiResult<CustomerDto> apiResult = (ApiResult<CustomerDto>) loginService.login(userLoginDto, loginInfoDto);

        if (apiResult.isSucc()) {
            apiResult.getData().setUserSession(userSessionPosComponent.add(session, new UserInfo(apiResult.getData())));
            // POS登陆的时候需要处理的逻辑，关系绑定切换到注册时绑定
            // posService.posLogin(apiResult.getData(), loginRequestDto.getType(), loginRequestDto.getLeaderId());
            apiResult.getData().setHeadImage(globalConstants.posHeadImage);
            apiResult.getData().setNickName(StringUtils.isNotBlank(apiResult.getData().getName()) ? apiResult.getData().getName() : apiResult.getData().getUserPhone());
        }

        return apiResult;
    }

}
