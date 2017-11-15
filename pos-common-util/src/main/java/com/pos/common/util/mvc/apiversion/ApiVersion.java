package com.pos.common.util.mvc.apiversion;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by 睿智 on 2017/9/11.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * 标识版本号
     * @return
     */
    int value();
}
