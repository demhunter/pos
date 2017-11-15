/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.basic.mq.MQMessage;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.basic.IpAddressUtils;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.basic.SimpleRegexUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.CustomerSourceType;
import com.pos.user.constant.UserGender;
import com.pos.user.constant.UserType;
import com.pos.user.dao.CustomerDao;
import com.pos.user.dao.UserDao;
import com.pos.user.domain.Customer;
import com.pos.user.domain.User;
import com.pos.user.domain.UserExtension;
import com.pos.user.dto.login.IdentityInfoDto;
import com.pos.user.dto.login.UserExtensionInfoDto;
import com.pos.user.dto.mq.CustomerInfoMsg;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.login.RegisterInfoDto;
import com.pos.user.dto.login.RegisterRecommendDto;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户注册服务接口类实现
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterServiceImpl implements RegisterService {

    private final static Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Resource
    private MQTemplate mqTemplate;

    @Resource
    private SmsService smsService;

    @Resource
    private UserDao userDao;

    @Resource
    private CustomerDao customerDao;

    @Override
    public ApiResult<CustomerDto> addCustomer(RegisterInfoDto registerInfo, boolean setLoginInfo) {
        IdentityInfoDto identity = registerInfo.getIdentityInfoDto();
        // 注册信息校验
        ErrorCode err = checkAddCustomer(identity.getLoginName(), identity.getPassword(), identity.getSmsCode());
        if (err != null) {
            return ApiResult.fail(err);
        }

        User existingUser = userDao.getByUserPhone(identity.getLoginName());
        if (existingUser == null) {
            // 用户不存在，注册一个新用户
            User user = addUser(identity.getLoginName(), identity.getPassword(), UserType.CUSTOMER, identity.getLoginName());
            Customer customer = saveCustomerBase(user);
            UserExtension extension = saveExtension(user, setLoginInfo, registerInfo.getUserExtensionInfo());
            CustomerDto customerDto = buildCustomerDto(user, customer, extension);

            // 发送注册推荐人消息
            sendCustomerRegisterMessage(user.getId(), user.getPhone(), registerInfo.getRegisterRecommend());

            return ApiResult.succ(customerDto);
        } else {
            // 用户已存在
            if (!existingUser.isEnable()) {
                return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
            }
            return ApiResult.fail(UserErrorCode.USER_EXISTED);
        }
    }

    /**
     * 客户注册信息校验
     *
     * @param userPhone  注册电话号码
     * @param password   注册密码
     * @param verifyCode 注册短信验证码
     * @return 校验结果
     */
    private ErrorCode checkAddCustomer(String userPhone, String password, String verifyCode) {
        Validator.checkMobileNumber(userPhone);
        Validator.checkPassword(password);

        if (userPhone.equals(password)) {
            return UserErrorCode.USER_PWD_DUPLICATE;
        }
        if (!smsService.checkVerifyCode(
                userPhone, verifyCode, MemcachedPrefixType.REGISTER).isSucc()) {
            return CommonErrorCode.VERIFY_CODE_ERROR;
        }

        return null;
    }

    /**
     * 添加一个用户账号
     *
     * @param loginName 登录账号名
     * @param password  登录密码
     * @param type      用户账号类型
     * @param phone     账号绑定的电话号码
     * @return 添加成功的用户账号信息
     */
    private User addUser(String loginName, String password, UserType type, String phone) {
        User user = new User();

        user.setLoginName(loginName);
        user.setPassword(MD5Utils.getMD5Code(password));
        user.setUserType(type.getValue());
        user.setEnableStatus(Boolean.TRUE);
        user.setPhone(phone);
        userDao.save(user);

        return user;
    }

    /**
     * 为用户账号补充C端客户信息
     *
     * @param user 用户账号信息
     * @return 成功添加的客户信息
     */
    private Customer saveCustomerBase(User user) {

        Customer customer = new Customer();
        customer.setUserId(user.getId());
        customer.setNickName("用户-" + SimpleRegexUtils.hiddenMobile(user.getLoginName()));
        customer.setSourceType(CustomerSourceType.SELF.getCode());
        customer.setGender(Integer.valueOf(UserGender.SECRECY.getCode()));
        customerDao.saveCustomerBase(customer);

        return customer;
    }

    /**
     * 保存用户拓展信息
     *
     * @param user          用户信息
     * @param setLoginInfo  是否记录登录信息
     * @param extensionInfo 拓展信息
     * @return 用户拓展信息
     */
    private UserExtension saveExtension(User user, boolean setLoginInfo, UserExtensionInfoDto extensionInfo) {
        Date now = Calendar.getInstance().getTime();

        UserExtension extension = new UserExtension();
        extension.setUserId(user.getId());
        // 查询IP所在地
        if (StringUtils.isNotEmpty(extensionInfo.getIp())) {
            extensionInfo.setIpAddress(IpAddressUtils.getAddresses(extensionInfo.getIp()));
        }
        // 设置拓展信息
        String extensionInfoJson = JsonUtils.objectToJson(extensionInfo);
        extension.setRegisterTime(now);
        extension.setRegisterInfo(extensionInfoJson);
        if (setLoginInfo) {
            extension.setLastLoginTime(now);
            extension.setLastLoginInfo(extensionInfoJson);
        }
        userDao.saveExtension(extension);

        return extension;
    }

    /**
     * 构建CustomerDto客户信息
     *
     * @param user      用户账号信息
     * @param customer  客户基本信息
     * @param extension 账号拓展信息
     * @return CustomerDto客户信息
     */
    private CustomerDto buildCustomerDto(User user, Customer customer, UserExtension extension) {
        CustomerDto customerDto = new CustomerDto();

        customerDto.setId(user.getId());
        customerDto.setLoginName(user.getLoginName());
        customerDto.setEnableStatus(user.getEnableStatus());
        customerDto.setUserType(user.getUserType());

        customerDto.setRegisterTime(extension.getRegisterTime());
        customerDto.setLastLoginTime(extension.getLastLoginTime());

        customerDto.setMail(customer.getMail());
        customerDto.setNickName(customer.getNickName());
        customerDto.setHeadImage(customer.getHeadImage());
        customerDto.setGender(customer.getGender());
        customerDto.setAge(customer.getAge());
        customerDto.setSourceType(customer.getSourceType());
        customerDto.setUpdateTime(customer.getUpdateTime());

        return customerDto;
    }

    private void sendCustomerRegisterMessage(Long userId, String userPhone, RegisterRecommendDto registerRecommend) {
        CustomerInfoMsg msg = new CustomerInfoMsg(userId, userPhone, registerRecommend);
        mqTemplate.sendDirectMessage(new MQMessage(MQReceiverType.CUSTOMER, "pos.register.route.key", msg));
        LOG.info("发送一条用户注册的消息");
    }
}
