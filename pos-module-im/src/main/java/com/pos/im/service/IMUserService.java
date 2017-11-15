/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service;

import com.pos.basic.dto.UserIdentifier;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dto.user.*;
import com.pos.im.exception.IMServerException;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * IM用户服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/7/6
 */
public interface IMUserService {

    /**
     * C端用户的类型值（此为冗余属性，不能修改！）
     */
    String USER_TYPE_CUSTOMER = "c";
    /**
     * B端用户的类型值（此为冗余属性，不能修改！）
     */
    String USER_TYPE_BUSINESS = "b";
    /**
     * B端从业者的类型值（此为冗余属性，不能修改！）
     */
    String USER_TYPE_EMPLOYEE = "e";
    /**
     * 平台管理员的类型值（此为冗余属性，不能修改！）
     */
    String USER_TYPE_MANAGER = "m";
    /**
     * 平台业者的细分类型（即家居顾问，此为冗余属性，不能修改！）
     */
    byte PLATFORM_EMPLOYEE_TYPE = 10;

    /**
     * APP获取用户访问IM的令牌.
     *
     * @param userInfo 用户信息
     * @return IM Token
     * @throws IMServerException 向IM Server申请Token失败
     */
    ApiResult<UserTokenDto> getToken(UserInfoDto userInfo) throws IMServerException;

    /**
     * APP获取TOKEN失败后记录错误消息.
     *
     * @param user              用户表示
     * @param userTokenErrorDto 失败的Token信息
     */
    void addTokenError(UserIdentifier user, UserTokenErrorDto userTokenErrorDto);

    /**
     * 为新注册的用户注册IM Server账号
     *
     * @param userInfo 用户信息
     * @throws IMServerException 本地保存用户信息成功，但往IM Server注册账号失败
     */
    void createIMUser(UserInfoDto userInfo) throws IMServerException;

    /**
     * 更新用户的IM信息
     *
     * @param userInfo 用户信息
     * @throws IMServerException 本地更新用户信息成功，但往IM Server更新失败
     */
    void updateIMUser(UserInfoDto userInfo) throws IMServerException;

    /**
     * 查询指定用户在IM中保存的信息.
     *
     * @param userId   用户ID
     * @param userType 用户类型
     * @param disable  是否返回被禁用的账号
     * @return
     */
    UserInfoDto findUser(Long userId, String userType, boolean disable);

    /**
     * 获取用户在IM中的问候语.
     *
     * @param user 用户标识
     * @return
     */
    ApiResult<UserGreetingDto> getUserGreetingLanguage(UserIdentifier user);

    /**
     * 更新用户在IM中的问候语.
     *
     * @param user           用户标识
     * @param appendGreeting 修改用户追加的问候语
     * @return
     */
    ApiResult<NullObject> updateUserGreetingLanguage(UserIdentifier user, String appendGreeting);

    /**
     * 获取用户在IM中的常用语.
     *
     * @param user 用户标识
     * @return
     */
    List<UserLanguageDto> getUserCommonLanguages(UserIdentifier user);

    /**
     * 用户添加一条IM常用语.
     *
     * @param user    用户标识
     * @param content 常用语内容
     * @return
     */
    ApiResult<UserLanguageDto> addUserCommonLanguage(UserIdentifier user, String content);

    /**
     * 用户删除指定的IM常用语.
     *
     * @param user       用户标识
     * @param languageId 常用语ID
     * @return
     */
    ApiResult<NullObject> delUserCommonLanguage(UserIdentifier user, Long languageId);

    /**
     * 构建IM Server accid
     *
     * @param user 用户标识
     * @return accid，格式：prefix-userType-userId，例如：dev-c-12345
     */
    String buildNeteaseIMAccid(UserIdentifier user);

    /**
     * 生成用户在第三方IM平台的UID.
     *
     * @param userId   用户ID
     * @param userType 用户类型
     * @return
     */
    @Deprecated
    static String buildIMUid(Long userId, String userType) {
        FieldChecker.checkEmpty(userId, "userId");
        FieldChecker.checkEmpty(userType, "userType");
        return userType + userId;
    }

    /**
     * 检查用户在第三方IM平台的UID是否合法.
     */
    static void checkIMUid(String imUid) {
        FieldChecker.checkEmpty(imUid, "imUid");
        String userType = imUid.substring(0, 1);
        if (!(USER_TYPE_CUSTOMER.equals(userType) || USER_TYPE_BUSINESS.equals(userType)
                || USER_TYPE_EMPLOYEE.equals(userType) || USER_TYPE_MANAGER.equals(userType))) {
            throw new IllegalParamException("不能解析用户的IMUid：" + imUid);
        }
        try {
            Long.parseLong(imUid.substring(1, imUid.length()));
        } catch (NumberFormatException e) {
            throw new IllegalParamException("不能解析用户的IMUid：" + imUid);
        }
    }

    /**
     * 根据用户在第三方IM平台的UID转换出UserInfoDto.
     *
     * @param imUid 第三方IM平台的UID，格式：prefix-userType-userId，例如：dev-c-12345
     * @return 只包含userId和userType，且不验证转换后值的有效性
     */
    static UserInfoDto parseIMUid(String imUid) {
        FieldChecker.checkEmpty(imUid, "imUid");
        UserInfoDto userInfoDto = new UserInfoDto();
        String[] str = StringUtils.split(imUid, "-");
        if (str.length != 3) {
            throw new IllegalParamException("不能解析用户的IMUid：" + imUid);
        }
        userInfoDto.setUserType(str[1]);
        try {
            userInfoDto.setUserId(Long.parseLong(str[2]));
        } catch (NumberFormatException e) {
            throw new IllegalParamException("不能解析用户的IMUid：" + imUid);
        }
        return userInfoDto;
    }

}
