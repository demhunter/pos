/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.service.support;

import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Validator;
import com.pos.transaction.constants.PosConstants;
import com.pos.user.constant.ManagerType;
import com.pos.user.constant.UserType;
import com.pos.user.dto.UserLoginDto;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.exception.UserErrorCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 快捷收款管理员相关接口
 * <p>
 *     应产品需求，此处管理员登录为固定账号密码校验。
 *     账号：***
 *     密码：***
 *     用户userId：10241024
 *     用户userType：m
 * </p>
 *
 * @author wangbing
 * @version 1.0, 2017/10/24
 */
@Component
public class PosAdminSupport {

    private static final Long userId = 10241024L; // 用户userId

    private static final UserType userType = UserType.MANAGER; // 用户userType

    @Resource
    private PosConstants posConstants;

    /**
     * 快捷收款管理员登录
     *
     * @param userLoginDto 登录信息
     * @return 登录结果
     */
    public ApiResult<ManagerDto> posAdminLogin(UserLoginDto userLoginDto) {
        // 参数校验
        checkUserLoginBefore(userLoginDto);
        // 登录校验
        if (!loginSucc(userLoginDto)) {
            return ApiResult.fail(UserErrorCode.USER_OR_PWD_ERROR);
        }
        // 登录成功，构建管理员信息
        return ApiResult.succ(buildManager());
    }

    private boolean loginSucc(UserLoginDto userLoginDto) {
        return posConstants.getPosAdminUserName().equals(userLoginDto.getLoginName())
                && posConstants.getPosAdminPassword().equals(userLoginDto.getPassword())
                && userType.equals(userLoginDto.getUserType());
    }

    private ManagerDto buildManager() {
        ManagerDto manager = new ManagerDto();
        manager.setId(userId);
        manager.setUserName(posConstants.getPosAdminUserName());
        manager.setUserType(userType.getValue());
        manager.setDeleted(Boolean.FALSE);
        manager.setAvailable(true);
        manager.setUserDetailType(ManagerType.DEFAULT.getCode());

        return manager;
    }

    @SuppressWarnings("all")
    private void checkUserLoginBefore(UserLoginDto userLoginDto) {
        FieldChecker.checkEmpty(userLoginDto, "userLoginDto");
        FieldChecker.checkEmpty(userLoginDto.getUserType(), "userLoginDto.userType");

        if (userLoginDto.getUserType().isUsePhone()) {
            Validator.checkMobileNumber(userLoginDto.getLoginName());
        } else {
            Validator.checkUserName(userLoginDto.getLoginName());
        }

        Validator.checkPassword(userLoginDto.getPassword());
        if (userLoginDto.getLoginName().equals(userLoginDto.getPassword())) {
            throw new ValidationException(UserErrorCode.USER_PWD_DUPLICATE.getMessage());
        }
    }
}
