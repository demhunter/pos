/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service_v;

import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.user.constant.UserType;

import java.util.List;

/**
 * 用户基础服务接口
 *
 * @author wangbing
 * @version 1.0, 2017/11/13
 */
public interface UserService {

    /**
     * 根据登陆账号名修改密码，短信验证码通过才可以进行修改.
     *
     * @param userPhone  手机号
     * @param newPwd     新密码
     * @param verifyCode 短信验证码
     * @param userType   用户类型
     */
    ApiResult<NullObject> updatePwdByPhone(
            String userPhone, String newPwd, String verifyCode, UserType userType);

    /**
     * 查询符合searchKey的C端用户id列表
     *
     * @param searchKey 搜索关键字
     * @return C端用户ID列表
     */
    List<Long> queryCustomerUserIds(String searchKey);
}
