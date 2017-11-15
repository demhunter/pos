/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 *
 * @author chichen  Date: 16-3-29 Time: 下午2:36
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static MappingJackson2JsonView jsonView = new MappingJackson2JsonView();

    public static final int DEFAULT_FAILED_STATUS = -1;

    private boolean serverError; // 是否向前端返回服务器内部错误信息

    public GlobalExceptionHandler(boolean serverError) {
        this.serverError = serverError;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView mav = new ModelAndView();
        mav.setView(jsonView);
        mav.addObject("succ", false);

        if (e instanceof RequiredParamException) {
            catchException(mav, CommonErrorCode.REQUIRED_PARAM, e);
        } else if (e instanceof IllegalParamException) {
            catchException(mav, CommonErrorCode.ILLEGAL_PARAM, e);
        } else if (e instanceof ValidationException) {
            catchException(mav, CommonErrorCode.VALIDATION_ERROR, e);
        } else {
            logger.error("系统异常，错误信息：{}", e.getMessage(), e);
            mav.addObject("stateCode", DEFAULT_FAILED_STATUS);
            if (!serverError) {
                mav.addObject("message", "服务器内部错误");
            } else {
                mav.addObject("message", "服务器内部错误: " + e.getMessage());
            }
        }

        return mav;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private void catchException(ModelAndView mav, ErrorCode ec, Exception ex) {
        mav.addObject("stateCode", ec.getCode());
        mav.addObject("error", ec);
        mav.addObject("message", ex.getMessage());
    }

}
