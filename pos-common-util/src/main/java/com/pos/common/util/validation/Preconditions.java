/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.validation;

import com.google.common.collect.Iterables;
import com.pos.common.util.exception.RequiredParamException;
import com.pos.common.util.exception.IllegalParamException;
import com.pos.common.util.exception.ValidationException;

import java.util.Collection;
import java.util.Map;

/**
 * 重写Google Guava Preconditions，用于检查参数异常<br/>
 * 有问题时将抛出 {@link ValidationException}
 *
 * @see com.google.common.base.Preconditions
 *
 * @author cc
 * @version 1.0, 16/8/8
 */
public final class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkCast(Object obj, Class<T> clazz) {
        if(!clazz.isInstance(obj)) {
            throw new ValidationException(format("expect %s but is %s", new Object[]{clazz, obj.getClass()}));
        } else {
            return clazz.cast(obj);
        }
    }

    public static <T> T checkCast(Object obj, Class<T> clazz, Object errorMessage) {
        if(!clazz.isInstance(obj)) {
            throw new ValidationException(String.valueOf(errorMessage));
        } else {
            return clazz.cast(obj);
        }
    }

    public static <T> T checkCast(Object obj, Class<T> clazz, String errorMessage, Object... errorMessageArgs) {
        if(!clazz.isInstance(obj)) {
            throw new ValidationException(format(errorMessage, errorMessageArgs));
        } else {
            return clazz.cast(obj);
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if(!expression) {
            throw new IllegalParamException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if(!expression) {
            throw new IllegalParamException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    @SuppressWarnings("all")
    public static void checkArgument(Object value, String fieldName) {
        if (value == null) {
            throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
        }
        if (value instanceof String) {
            if (((String) value).trim().length() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        } else if (value instanceof Collection<?>) {
            if (((Collection<?>) value).size() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        } else if (value instanceof Map<?, ?>) {
            if (((Map<?, ?>) value).size() == 0) {
                throw new RequiredParamException("缺少必需的参数：" + fieldName + "");
            }
        }
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if(reference == null) {
            throw new RequiredParamException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static void checkArgsNotNull(Object... args) {
        for (Object arg : args) {
            checkNotNull(arg, "参数不能为空！");
        }
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if(reference == null) {
            throw new RequiredParamException(format(errorMessageTemplate, errorMessageArgs));
        } else {
            return reference;
        }
    }

    public static <T> Iterable<T> checkNotEmpty(Iterable<T> reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if(Iterables.isEmpty(reference)) {
            throw new IllegalParamException(format(errorMessageTemplate, errorMessageArgs));
        } else {
            return reference;
        }
    }

    static String format(String template, Object... args) {
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;

        int i;
        int placeholderStart;
        for(i = 0; i < args.length; templateStart = placeholderStart + 2) {
            placeholderStart = template.indexOf("%s", templateStart);
            if(placeholderStart == -1) {
                break;
            }

            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
        }

        builder.append(template.substring(templateStart));
        if(i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);

            while(i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }

            builder.append(']');
        }

        return builder.toString();
    }
}
