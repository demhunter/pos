/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.common.util.mvc.support.*;
import com.pos.basic.dto.UserIdentifier;
import com.pos.user.constant.UserType;
import com.pos.user.dto.feedback.UserFeedbackDto;
import com.pos.user.condition.orderby.UserFeedbackOrderField;
import com.pos.user.dto.UserAuthInfoDto;
import com.pos.user.dto.UserDto;
import com.pos.user.dto.feedback.NewFeedbackDto;

import java.util.List;

/**
 * 用户基础服务接口.
 *
 * @author wayne
 * @version 1.0, 2016/6/7
 */
public interface UserService {

    /**
     * 更新User
     *
     * @param userDto user信息
     */
    void update(UserDto userDto);

    /**
     * 查询身份证号是否已存在.
     *
     * @param idNumber 身份证号
     * @return 存在返回true, 否则返回false
     */
    boolean isIdNumberExists(String idNumber);

    /**
     * 查询身份证号是否已存在.
     *
     * @param userId   用户ID
     * @param idNumber 身份证号
     * @return 如果该身份证号存在且与当前用户的身份证号不匹配时返回true, 否则返回false
     */
    boolean isIdNumberExists(Long userId, String idNumber);

    /**
     * 根据用户标识获取用户类型ID.
     *
     * @param user 用户标识
     * @return
     */
    Long getUserClassId(UserIdentifier user);

    /**
     * 根据用户标识获取其认证信息.
     *
     * @param user 用户标识
     * @return
     */
    UserAuthInfoDto getUserAuthInfo(UserIdentifier user);

    /**
     * 根据用户ID将用户密码重置为系统默认密码.
     *
     * @param userId 用户ID
     */
    ApiResult<NullObject> resetPwdById(Long userId);

    /**
     * 根据用户ID修改密码，旧密码验证通过才可以进行修改.
     *
     * @param userId 用户ID
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    ApiResult<NullObject> updatePwdById(Long userId, String oldPwd, String newPwd);

    /**
     * 根据手机号修改密码，短信验证码通过才可以进行修改.
     *
     * @param userPhone  手机号
     * @param newPwd     新密码
     * @param verifyCode 短信验证码
     * @param userType   用户类型
     */
    ApiResult<NullObject> updatePwdByPhone(
            String userPhone, String newPwd, String verifyCode, UserType userType);

    /**
     * 首次登陆时重设密码，TOKEN验证通过才可以进行修改.
     *
     * @param userName 用户名
     * @param newPwd   新密码
     * @param token    修改令牌
     * @param userType 用户类型
     */
    ApiResult<NullObject> initPwdByUserName(
            String userName, String newPwd, String token, UserType userType);

    /**
     * 根据用户ID修改其指定类型的账号状态.
     *
     * @param userId    用户ID
     * @param userType  用户类型
     * @param available 是否可用
     */
    ApiResult<NullObject> updateStatus(Long userId, UserType userType, boolean available);

    /**
     * 添加一个反馈意见.
     *
     * @param userId      用户ID（可以为空）
     * @param feedbackDto 反馈信息
     */
    ApiResult<NullObject> addFeedback(Long userId, NewFeedbackDto feedbackDto);

    /**
     * 查询反馈意见列表.
     *
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link UserFeedbackOrderField}
     * @return
     */
    Pagination<UserFeedbackDto> findFeedbackList(LimitHelper limitHelper, OrderHelper orderHelper);

    /**
     * 查询符合searchKey的用户id列表
     *
     * @param searchKey 搜索关键字
     * @return 用户ID列表
     */
    List<Long> queryUserIds(String searchKey);
}
