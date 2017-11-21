/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.dao.TemplateDao;
import com.pos.basic.service.TemplateService;
import com.pos.common.util.validation.Preconditions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * TemplateServiceImpl
 *
 * @author cc
 * @version 1.0, 2017/3/7
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private TemplateDao templateDao;

    @Override
    public String queryTemplateById(Long id) {
        Preconditions.checkArgsNotNull(id);

        return templateDao.queryTemplateById(id);
    }
}
