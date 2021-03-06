/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.transaction.dao;

import com.pos.transaction.dto.card.PosCardDto;
import com.pos.transaction.constants.CardUsageEnum;
import com.pos.transaction.domain.UserPosCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * POS 银行卡相关Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/18
 */
@Repository
public interface PosCardDao {

    /**
     * 保存银行卡信息
     *
     * @param userPosCard 银行卡信息
     */
    void save(@Param("cardInfo") UserPosCard userPosCard);

    /**
     * 查询用户的银行卡信息
     *
     * @param userId    用户userId
     * @param cardUsage 银行卡的使用类型 {@link CardUsageEnum#code}
     * @return 银行卡信息列表
     */
    List<PosCardDto> queryUserPosCard(
            @Param("userId") Long userId,
            @Param("cardUsage") Byte cardUsage);

    /**
     * 查询指定的银行卡信息
     *
     * @param cardIds 银行卡id列表
     * @return 银行卡信息
     */
    List<PosCardDto> queryPosCardByIds(@Param("cardIds") List<Long> cardIds);

    /**
     * 获取指定的银行卡信息
     *
     * @param cardId 银行卡id
     * @return 银行卡信息
     */
    PosCardDto getUserPosCard(@Param("cardId") Long cardId);

    /**
     * 删除指定的银行卡信息
     *
     * @param cardId 银行卡id
     */
    void delete(@Param("cardId") Long cardId);

    /**
     * 更新银行卡信息
     *
     * @param card 银行卡信息
     */
    void update(@Param("card") UserPosCard card);

    /**
     * 查询指定的银行卡信息
     *
     * @param cardIds 银行卡id列表
     * @return 银行卡信息
     */
    List<PosCardDto> queryByCardIds(@Param("cardIds") List<Long> cardIds);
}
