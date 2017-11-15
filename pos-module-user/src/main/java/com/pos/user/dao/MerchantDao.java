/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.user.domain.Merchant;
import com.pos.user.dto.merchant.MerchantDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 平台管理员相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/8/1
 */
@Repository
public interface MerchantDao {

    void save(@Param("m") Merchant merchant);

    void update(@Param("m") Merchant merchant);

    Merchant getByUserId(@Param("userId") Long userId);

    MerchantDto findMerchantByUserId(
            @Param("userId") Long userId,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    MerchantDto findMerchantByUserName(
            @Param("userName") String userName,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    MerchantDto findMerchantByUserPhone(
            @Param("userPhone") String userPhone,
            @Param("deleted") Boolean deleted, @Param("available") Boolean available);

    List<MerchantDto> queryMerchants(
            @Param("deleted") Boolean deleted,
            @Param("available") Boolean available,
            @Param("limitHelper") LimitHelper limitHelper);

}
