/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.impl;

import com.pos.common.util.validation.FieldChecker;
import com.pos.im.dao.CompanyInfoDao;
import com.pos.im.domain.CompanyInfo;
import com.pos.im.dto.company.CompanyInfoDto;
import com.pos.im.service.IMCompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IM公司服务实现类.
 *
 * @author wayne
 * @version 1.0, 2016/12/6
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class IMCompanyServiceImpl implements IMCompanyService {

    @Resource
    private CompanyInfoDao companyInfoDao;

    @Override
    public void refresh(CompanyInfoDto companyInfoDto) {
        FieldChecker.checkEmpty(companyInfoDto, "companyInfoDto");
        FieldChecker.checkEmpty(companyInfoDto.getCompanyId(), "companyInfoDto.companyId");

        CompanyInfo persistent = convert2CompanyInfo(companyInfoDto);
        CompanyInfo companyInfo = companyInfoDao.findByCompanyId(companyInfoDto.getCompanyId());
        if (companyInfo != null) {
            persistent.setId(companyInfo.getId());
            persistent.setUpdateTime(Calendar.getInstance().getTime());
            companyInfoDao.update(persistent);
        } else {
            persistent.setCreateTime(Calendar.getInstance().getTime());
            persistent.setUpdateTime(persistent.getCreateTime());
            if (persistent.getAvailable() == null) {
                persistent.setAvailable(Boolean.TRUE);
            }
            if (persistent.getImSmsEnable() == null) {
                persistent.setImSmsEnable(Boolean.TRUE);
            }
            if (persistent.getImNoticeEnable() == null) {
                persistent.setImNoticeEnable(Boolean.TRUE);
            }
            companyInfoDao.save(persistent);
        }
    }

    @Override
    public List<CompanyInfoDto> findCompaniesBySessionId(Long sessionId) {
        FieldChecker.checkEmpty(sessionId, "sessionId");
        List<CompanyInfo> list = companyInfoDao.findCompaniesBySessionId(sessionId);
        if (list != null && !list.isEmpty()) {
            return list.stream().map(company ->
                    convert2CompanyInfoDto(company)).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    private CompanyInfo convert2CompanyInfo(CompanyInfoDto companyInfoDto) {
        CompanyInfo companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyInfoDto, companyInfo);
        return companyInfo;
    }

    private CompanyInfoDto convert2CompanyInfoDto(CompanyInfo companyInfo) {
        CompanyInfoDto companyInfoDto = new CompanyInfoDto();
        BeanUtils.copyProperties(companyInfo, companyInfoDto);
        return companyInfoDto;
    }

}