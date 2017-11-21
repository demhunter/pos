/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.basic.log.UserActionEnum;
import com.pos.basic.log.UserActionResourceEnum;
import com.pos.basic.log.UserActionTypeEnum;
import com.pos.user.dto.DataLogDto;
import com.pos.user.dao.DataLogDao;
import com.pos.user.domain.DataLog;
import com.pos.user.service.DataLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 睿智
 * @version 1.0, 2017/10/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataLogServiceImpl implements DataLogService {

    @Resource
    private DataLogDao dataLogDao;

    @Override
    public void saveDataLog(DataLogDto dataLogDto) {
        DataLog dataLog = new DataLog();
        BeanUtils.copyProperties(dataLogDto,dataLog);
        dataLog.setActionResource(dataLogDto.getActionResource()!=0?UserActionResourceEnum.getEnum(dataLogDto.getActionResource()).getDesc():"");
        dataLog.setActionResult(String.valueOf(dataLogDto.getActionResult()));
        int action = dataLogDto.getAction();
        dataLog.setAction(UserActionEnum.getEnum(action).getDesc());
        switch (UserActionTypeEnum.getEnum(UserActionEnum.getEnum(action).getType())){
            case ACTION_TYPE_APP:
                dataLogDao.saveAppLog(dataLog);
                break;
            case ACTION_TYPE_ACCOUNT:
                dataLogDao.saveAccountLog(dataLog);
                break;
            case ACTION_TYPE_CASE:
                dataLogDao.saveCaseLog(dataLog);
                break;
            case ACTION_TYPE_DISCOUNT:
                dataLogDao.saveDiscountLog(dataLog);
                break;
            case ACTION_TYPE_GUARANTEE:
                dataLogDao.saveGuaranteeLog(dataLog);
                break;
            case ACTION_TYPE_CHAT:
                dataLogDao.saveChatLog(dataLog);
                break;
            case ACTION_TYPE_MY:
                dataLogDao.saveMyLog(dataLog);
                break;
            default:
                break;
        }
    }
}
