/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dao;

import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.OrderHelper;
import com.pos.user.domain.UserFeedback;
import com.pos.user.dto.feedback.UserFeedbackDto;
import com.pos.user.condition.orderby.UserFeedbackOrderField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户反馈相关DAO.
 *
 * @author wayne
 * @version 1.0, 2016/10/9
 */
@Repository
public interface UserFeedbackDao {

    void save(@Param("uf") UserFeedback uf);

    /**
     * 统计用户反馈意见的总数.
     *
     * @return 用户反馈意见的总条数
     */
    int getTotal();

    /**
     * 分页查询用户反馈意见列表.
     *
     * @param limitHelper 分页参数
     * @param orderHelper 排序参数, 可选值: {@link UserFeedbackOrderField}
     * @return
     */
    List<UserFeedbackDto> findList(@Param("limitHelper") LimitHelper limitHelper,
                                   @Param("orderHelper") OrderHelper orderHelper);

}