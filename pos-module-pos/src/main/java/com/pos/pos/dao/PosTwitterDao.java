/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.pos.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.pos.domain.Twitter;
import com.pos.pos.domain.TwitterCustomer;
import com.pos.pos.domain.TwitterRelation;
import com.pos.pos.dto.develop.ChildTwitterDto;
import com.pos.pos.dto.spread.SpreadCustomerDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推客相关Dao
 *
 * @author wangbing
 * @version 1.0, 2017/10/21
 */
@Repository
public interface PosTwitterDao {

    /**
     * 根据userId获取推客信息
     *
     * @param userId 推客userId
     * @return 推客信息
     */
    Twitter getTwitterByUserId(@Param("userId") Long userId);

    /**
     * 根据twitterId获取推客信息
     *
     * @param twitterId 推客Id
     * @return 推客信息
     */
    Twitter getTwitterById(@Param("twitterId") Long twitterId);

    /**
     * 根据childTwitterId获取父推客信息
     *
     * @param childTwitterId 子推客Id
     * @return 父推客信息
     */
    Twitter getParentTwitterByChild(@Param("childTwitterId") Long childTwitterId);

    /**
     * 根据客户userId获取推客客户关系
     *
     * @param userId 客户用户id
     * @return 推客客户信息
     */
    TwitterCustomer getTwitterCustomerByUserId(@Param("userId") Long userId);

    /**
     * 新增推客
     *
     * @param twitter 推客信息
     */
    void saveTwitter(@Param("twitter") Twitter twitter);

    /**
     * 更新推客信息
     *
     * @param twitter 渠道商信息
     */
    void update(@Param("twitter") Twitter twitter);

    /**
     * 保存推客关联关系
     *
     * @param relation 关联关系
     */
    void saveTwitterRelation(@Param("relation") TwitterRelation relation);

    /**
     * 更新推客关联关系
     *
     * @param relation 关联关系
     */
    void updateTwitterRelation(@Param("relation") TwitterRelation relation);

    /**
     * 根据子推客id获取推客关联关系
     *
     * @param childTwitterId 子推客id
     */
    TwitterRelation getTwitterRelationByChildId(@Param("childTwitterId") Long childTwitterId);

    /**
     * 根据子推客id更新推客推客关联关系
     *
     * @param childTwitterId 子推客id
     * @param available      true：恢复关联；false：取消关联
     * @param updateUserId   更新操作人UserId
     */
    void updateTwitterRelationAvailableByChild(
            @Param("childTwitterId") Long childTwitterId,
            @Param("available") boolean available,
            @Param("updateUserId") Long updateUserId);

    /**
     * 根据父推客id更新推客推客关联关系
     *
     * @param parentTwitterId 父推客id
     * @param available       true：恢复关联；false：取消关联
     * @param updateUserId    更新操作人UserId
     */
    void updateTwitterRelationAvailableByParent(
            @Param("parentTwitterId") Long parentTwitterId,
            @Param("available") boolean available,
            @Param("updateUserId") Long updateUserId);

    /**
     * 保存推客客户关系
     *
     * @param customer 推客客户关系
     */
    void saveTwitterCustomer(@Param("junior") TwitterCustomer customer);

    /**
     * 更新推客客户关系
     *
     * @param customer 推客客户关系
     */
    void updateTwitterCustomer(@Param("customer") TwitterCustomer customer);

    /**
     * 根据推客id更新推客客户关联关系
     *
     * @param twitterUserId 推客userId
     * @param available     true：恢复关联；false：取消关联
     * @param updateUserId  更新操作人UserId
     */
    void updateTwitterCustomerAvailableByTwitter(
            @Param("twitterUserId") Long twitterUserId,
            @Param("available") boolean available,
            @Param("updateUserId") Long updateUserId);

    /**
     * 查询用户发展的下级推客数量
     *
     * @param parentUserId 推客userId
     * @return 下级推客数量
     */
    int getDevelopCountByParentUserId(@Param("parentUserId") Long parentUserId);

    /**
     * 查询用户发展的下级推客列表
     *
     * @param parentUserId 用户id
     * @param limitHelper  分页参数
     * @return 查询结果
     */
    List<ChildTwitterDto> queryDevelopChildTwitter(
            @Param("parentUserId") Long parentUserId,
            @Param("limitHelper") LimitHelper limitHelper);

    /**
     * 查询推客发展的有效客户数
     *
     * @param twitterUserId 推客用户userId
     * @return 发展的客户数
     */
    int queryCustomerCountByTwitterUserId(@Param("twitterUserId") Long twitterUserId);

    /**
     * 查询推客用户发展的客户列表
     *
     * @param twitterUserId 推客用户userId
     * @param limitHelper   分页参数
     * @return 推客用户发展的客户列表
     */
    List<SpreadCustomerDto> queryCustomersByTwitterUserId(
            @Param("twitterUserId") Long twitterUserId,
            @Param("limitHelper") LimitHelper limitHelper);

}
