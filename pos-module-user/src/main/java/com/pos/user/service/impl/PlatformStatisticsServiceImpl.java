/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.user.dto.statistics.PlatformEmployeeStatisticsDto;
import com.pos.user.constant.CustomerOrderStatus;
import com.pos.user.dao.PlatformEmployeeDao;
import com.pos.user.dao.PlatformStatisticsDao;
import com.pos.user.domain.PlatformEmployee;
import com.pos.user.dto.statistics.CountDto;
import com.pos.user.dto.statistics.PlatformCustomerStatisticsDto;
import com.pos.user.service.PlatformStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/7/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PlatformStatisticsServiceImpl implements PlatformStatisticsService {

    @Resource
    private PlatformStatisticsDao platformStatisticsDao;

    @Resource
    private PlatformEmployeeDao platformEmployeeDao;

    @Override
    public void updatePlatformEmployeeStatistics(long userId,long peId) {
        if(userId==0){
            PlatformEmployee platformEmployee = platformEmployeeDao.queryById(peId);
            userId = platformEmployee.getUserId();
        }
        PlatformEmployeeStatisticsDto platformEmployeeStatisticsDto = platformStatisticsDao
                .getPlatformEmployeeStatisticsByUserId(userId);
        if (platformEmployeeStatisticsDto == null) {
            platformEmployeeStatisticsDto = new PlatformEmployeeStatisticsDto();
            platformEmployeeStatisticsDto.setUserId(userId);
            platformEmployeeStatisticsDto = buildPlatformEmployeeData(userId,platformEmployeeStatisticsDto);
            platformStatisticsDao.addPlatformEmployeeStatistics(platformEmployeeStatisticsDto);
        } else {
            platformEmployeeStatisticsDto = buildPlatformEmployeeData(userId,platformEmployeeStatisticsDto);
            platformStatisticsDao.updatePlatformEmployeeStatistics(platformEmployeeStatisticsDto);
        }
    }

    @Override
    public void updatePlatformCustomerStatistics(long userId) {
        PlatformCustomerStatisticsDto platformCustomerStatisticsDto = platformStatisticsDao
                .getPlatformCustomerStatisticsByUserId(userId);
        if (platformCustomerStatisticsDto == null) {
            platformCustomerStatisticsDto = new PlatformCustomerStatisticsDto();
            platformCustomerStatisticsDto.setUserId(userId);
            platformCustomerStatisticsDto = buildPlatformCustomerData(userId,platformCustomerStatisticsDto);
            platformStatisticsDao.addPlatformCustomerStatistics(platformCustomerStatisticsDto);
        } else {
            platformCustomerStatisticsDto = buildPlatformCustomerData(userId,platformCustomerStatisticsDto);
            platformStatisticsDao.updatePlatformCustomerStatistics(platformCustomerStatisticsDto);
        }
    }

    private PlatformEmployeeStatisticsDto buildPlatformEmployeeData(Long userId,PlatformEmployeeStatisticsDto platformEmployeeStatisticsDto){
        int customerTalking = 0;//谈单中的客户数
        int customerComplete = 0;//已成单客户数
        int customerRefuse = 0;//已飞单客户数
        int imSessionAvailable = 0;//未关闭会话数
        int imSessionClosed = 0;//已关闭会话数
        int orderUnpaid = 0;//待支付订单数
        int orderGoing = 0;//进行中的订单数
        int orderFinished = 0;//已完成的订单数
        int orderTerminated = 0;//已关闭的订单数
        int twitter = 0;//关联的推客数
        List<CountDto> customerCounts = platformStatisticsDao.queryCustomerCount(userId);
        for(CountDto countDto:customerCounts){
            switch (CustomerOrderStatus.getEnum((byte)countDto.getStatus())){
                case TALKORDER://谈单中
                    customerTalking += countDto.getCount();
                    break;
                case COMPLETEORDER://已成单
                    customerComplete += countDto.getCount();
                    break;
                case REFUSEORDER://已飞单
                    customerRefuse += countDto.getCount();
                    break;
                default:
                    break;
            }
        }
        List<CountDto> sessionCounts = platformStatisticsDao.querySessionCount(userId);
        for(CountDto countDto:sessionCounts){
            if(countDto.getStatus()==1){//未关闭会话
                imSessionAvailable += countDto.getCount();
            }else{//已关闭会话
                imSessionClosed += countDto.getCount();
            }
        }
        List<CountDto> orderCounts = platformStatisticsDao.queryOrderCount(userId);
        for(CountDto countDto:orderCounts){
            if(countDto.getStatus()==0){//待付款
                orderUnpaid += countDto.getCount();
            }else if(countDto.getStatus()==1){//进行中
                orderGoing += countDto.getCount();
            }else if(countDto.getStatus()==2){//已完成
                orderFinished += countDto.getCount();
            }else{//已关闭
                orderTerminated += countDto.getCount();
            }
        }
        twitter = platformStatisticsDao.queryTwitterCount(userId);
        platformEmployeeStatisticsDto.setCustomerComplete(customerComplete);
        platformEmployeeStatisticsDto.setCustomerRefuse(customerRefuse);
        platformEmployeeStatisticsDto.setCustomerTalking(customerTalking);
        platformEmployeeStatisticsDto.setImSessionAvailable(imSessionAvailable);
        platformEmployeeStatisticsDto.setImSessionClosed(imSessionClosed);
        platformEmployeeStatisticsDto.setOrderFinished(orderFinished);
        platformEmployeeStatisticsDto.setOrderGoing(orderGoing);
        platformEmployeeStatisticsDto.setOrderTerminated(orderTerminated);
        platformEmployeeStatisticsDto.setOrderUnpaid(orderUnpaid);
        platformEmployeeStatisticsDto.setTwitter(twitter);
        return platformEmployeeStatisticsDto;
    }

    private PlatformCustomerStatisticsDto buildPlatformCustomerData(Long userId,PlatformCustomerStatisticsDto platformCustomerStatisticsDto){
        int imSessionAvailable = 0;//未关闭会话数
        int imSessionClosed = 0;//已关闭会话数
        int orderUnpaid = 0;//待支付订单数
        int orderGoing = 0;//进行中的订单数
        int orderFinished = 0;//已完成的订单数
        int orderTerminated = 0;//已关闭的订单数
        List<CountDto> sessionCounts = platformStatisticsDao.querySessionCount(userId);
        for(CountDto countDto:sessionCounts){
            if(countDto.getStatus()==1){//未关闭会话
                imSessionAvailable += countDto.getCount();
            }else{//已关闭会话
                imSessionClosed += countDto.getCount();
            }
        }
        List<CountDto> orderCounts = platformStatisticsDao.queryCustomerOrderCount(userId);
        for(CountDto countDto:orderCounts){
            if(countDto.getStatus()==0){//待付款
                orderUnpaid += countDto.getCount();
            }else if(countDto.getStatus()==1){//进行中
                orderGoing += countDto.getCount();
            }else if(countDto.getStatus()==2){//已完成
                orderFinished += countDto.getCount();
            }else{//已关闭
                orderTerminated += countDto.getCount();
            }
        }
        platformCustomerStatisticsDto.setImSessionAvailable(imSessionAvailable);
        platformCustomerStatisticsDto.setImSessionClosed(imSessionClosed);
        platformCustomerStatisticsDto.setOrderFinished(orderFinished);
        platformCustomerStatisticsDto.setOrderGoing(orderGoing);
        platformCustomerStatisticsDto.setOrderTerminated(orderTerminated);
        platformCustomerStatisticsDto.setOrderUnpaid(orderUnpaid);
        return platformCustomerStatisticsDto;
    }
}
