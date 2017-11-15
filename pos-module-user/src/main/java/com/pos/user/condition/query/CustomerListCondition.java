package com.pos.user.condition.query;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 客户列表查询条件
 *
 * Created by WangShengzhi on 2016/7/14.
 */
public class CustomerListCondition implements Serializable {

    private Long companyId;

    private String queryKey;

    private String orderBy;

    private Boolean asc;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
