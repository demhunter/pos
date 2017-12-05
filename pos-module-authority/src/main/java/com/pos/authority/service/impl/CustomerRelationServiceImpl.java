/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.authority.service.impl;

import com.pos.authority.dao.CustomerRelationDao;
import com.pos.authority.dto.relation.CustomerRelationDto;
import com.pos.authority.service.CustomerRelationService;
import com.pos.common.util.validation.FieldChecker;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户关系ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/5
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerRelationServiceImpl implements CustomerRelationService {

    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerRelationDao customerRelationDao;

    @Override
    public CustomerRelationDto getRelation(Long childUserId) {
        FieldChecker.checkEmpty(childUserId, "childUserId");

        return customerRelationDao.getByChildUserId(childUserId);
    }

    @Override
    public CustomerDto getParentCustomer(Long childUserId) {
        FieldChecker.checkEmpty(childUserId, "childUserId");

        CustomerRelationDto relation = customerRelationDao.getByChildUserId(childUserId);
        CustomerDto parent = null;
        if (relation != null) {
            if (relation.getParentUserId().equals(0L)) {
                // 当前用户不存在上级
                parent = new CustomerDto();
                parent.setId(0L);
            } else {
                parent = customerService.findById(relation.getParentUserId(), true, true);
            }
        }

        return parent;
    }
}
