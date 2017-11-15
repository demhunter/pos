/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.converter;

import com.pos.basic.domain.ServiceContent;
import com.pos.basic.dto.ServiceContentDto;
import org.springframework.beans.BeanUtils;

/**
 * 服务内容Converter
 *
 * @author wangbing
 * @version 1.0, 2016/12/15
 */
public class ServiceContentConverter {

    public static ServiceContent toServiceContent(ServiceContentDto serviceContentDto) {
        ServiceContent serviceContent = new ServiceContent();

        BeanUtils.copyProperties(serviceContentDto, serviceContent);

        return serviceContent;
    }

    public static ServiceContentDto toServiceContentDto(ServiceContent serviceContent) {
        ServiceContentDto serviceContentDto = new ServiceContentDto();

        BeanUtils.copyProperties(serviceContent, serviceContentDto);

        return serviceContentDto;
    }
}
