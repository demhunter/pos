/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.data.repair.v2_0_1;

import com.google.common.collect.Lists;
import com.pos.authority.domain.CustomerLevelConfig;
import com.pos.authority.domain.CustomerPermission;
import com.pos.authority.service.support.CustomerLevelSupport;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.data.repair.dao.RepairV2_0_1Dao;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * POS V2.0.0数据修复使用
 *
 * @author wangbing
 * @version 1.0, 2018/1/25
 */
public class DataRepairV2_0_1 {

    private static final Logger LOG = LoggerFactory.getLogger(DataRepairV2_0_1.class);

    @Resource
    private RepairV2_0_1Dao repairV2_0_1Dao;

    @Resource
    private CustomerLevelSupport customerLevelSupport;

    @Resource
    private CustomerService customerService;

    @Resource
    private SmsService smsService;

    /**
     * 新版本发布，晋升客户等级
     *
     * @return 被晋升的用户id列表
     */
    public ApiResult<List<Long>> updateCustomerLevel() {

        List<CustomerPermission> permissions = repairV2_0_1Dao.getPermissionsByLevel(1);
        if (!CollectionUtils.isEmpty(permissions)) {
            CustomerLevelConfig config = customerLevelSupport.getLevelConfig(2);
            if (config == null) {
                return ApiResult.fail(new ErrorCode() {
                    @Override
                    public int getCode() {
                        return -1;
                    }

                    @Override
                    public String getMessage() {
                        return "当前没有LV2等级配置，无法为用户晋升等级";
                    }
                });
            }
            List<Long> userIds = Lists.newArrayList();
            for (CustomerPermission permission : permissions) {
                permission.setLevel(config.getLevel());
                permission.setWithdrawRate(config.getWithdrawRate());
                permission.setExtraServiceCharge(config.getExtraServiceCharge());

                repairV2_0_1Dao.updateLevelInfo(permission);

                userIds.add(permission.getUserId());
            }

            sendLevelUpdateMsg(userIds, config);

            return ApiResult.succ(userIds);
        } else {
            LOG.info("当前没有符合升级条件的用户");
            return ApiResult.succ();
        }
    }

    private void sendLevelUpdateMsg(List<Long> userIds, CustomerLevelConfig config) {
        List<CustomerDto> customers = customerService.findInUserIds(userIds, true, true);
        if (!CollectionUtils.isEmpty(customers)) {

            String rateStr = config.getWithdrawRate().multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toPlainString()
                    + "%+"
                    + config.getExtraServiceCharge().toPlainString()
                    + "元";

            String msg = "尊敬的钱刷刷用户你好，新版本新模式，你的等级已晋升到LV2，收款费率减少到" + rateStr + "，赶快来体验吧！";
            List<String> phones = customers.stream().map(CustomerDto::getUserPhone).collect(Collectors.toList());
            smsService.sendMessageBatch(phones, msg);
        }
    }
}
