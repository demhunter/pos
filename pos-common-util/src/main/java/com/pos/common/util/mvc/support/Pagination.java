/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.support;

import com.google.common.base.Strings;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * Created by cc on 16/6/7.
 */
public class Pagination<T> implements Serializable {

    private static final long serialVersionUID = -2572590131674416220L;

    /**
     * 总量
     */
    @ApiModelProperty("总量")
    private int totalCount;

    /**
     * 当前页数
     */
    @ApiModelProperty("当前页数")
    private int currentPageNum;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private int totalPageNum;

    /**
     * 上一页Uri
     */
    @ApiModelProperty("上一页Uri")
    private String previousUri;

    /**
     * 当前页Uri
     */
    @ApiModelProperty("当前页Uri")
    private String currentUri;

    /**
     * 下一页Uri
     */
    @ApiModelProperty("下一页Uri")
    private String nextUri;

    /**
     * 分页查询结果
     */
    @ApiModelProperty("分页查询结果")
    private List<T> result;

    /**
     * 构造函数
     *
     * @param totalCount 总数量
     * @param currentPageNum 当前页数
     * @param pageSize 每页大小
     * @param ordering 排序规则
     * @param requestUri 请求Uri
     * @param result 分页查询结果
     */
    public Pagination(int totalCount, int currentPageNum, int pageSize, String ordering, String requestUri, List<T> result) {
        this.currentPageNum = currentPageNum;
        this.totalPageNum = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.totalCount = totalCount;
        this.result = result;

        if (!Strings.isNullOrEmpty(requestUri)) {
            this.currentUri = buildUriWithParams(requestUri, this.currentPageNum, pageSize, ordering);
            if (this.currentPageNum <= 1) {
                this.previousUri = null;
            } else {
                this.previousUri = buildUriWithParams(requestUri, this.currentPageNum - 1, pageSize, ordering);
            }
            if (this.currentPageNum >= totalPageNum) {
                this.nextUri = null;
            } else {
                this.nextUri = buildUriWithParams(requestUri, this.currentPageNum + 1, pageSize, ordering);
            }
        }
    }

    public Pagination(int totalCount, LimitHelper limitHelper, String ordering, String requestUri, List<T> result) {
        this(totalCount, limitHelper.getPageNum(), limitHelper.getPageSize(), ordering, requestUri, result);
    }

    public Pagination(int totalCount, LimitHelper currentPage, String requestUri, List<T> result) {
        this(totalCount, currentPage, null, requestUri, result);
    }

    public Pagination(int totalCount, int currentPageNum, int pageSize, String requestUri, List<T> result) {
        this(totalCount, currentPageNum, pageSize, null, requestUri, result);
    }

    public Pagination(int totalCount, LimitHelper limiter, List<T> result) {
        this(totalCount, limiter.getPageNum(), limiter.getPageSize(), result);
    }

    public Pagination(int totalCount, int currentPageNum, int pageSize, List<T> result) {
        this(totalCount, currentPageNum, pageSize, null, null, result);
    }

    public Pagination() {
    }

    /**
     * 构建带参数的Uri
     *
     * @param requestUri 不带参数的Uri
     * @param pageNum 页数
     * @param pageSize 每页大小
     * @param ordering 排序规则
     * @return 带参数的Uri
     */
    @SuppressWarnings("all")
    private static String buildUriWithParams(String requestUri, int pageNum, int pageSize, String ordering) {
        StringBuilder stringBuilder = new StringBuilder(requestUri + '?');
        stringBuilder.append("pageNum=" + pageNum + "&");
        stringBuilder.append("pageSize=" + pageSize + "&");
        if (StringUtils.isNotEmpty(ordering)) {
            stringBuilder.append("ordering=" + ordering);
        } else {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    /**
     * 根据分页参数和总行数创建一个Pagination实例.
     *
     * @param limitHelper 分页参数组件
     * @param totalCount 总行数
     */
    public static Pagination newInstance(LimitHelper limitHelper, int totalCount) {
        Pagination pagination = new Pagination();
        pagination.setTotalCount(totalCount);
        if (limitHelper != null) {
            pagination.setCurrentPageNum(limitHelper.getPageNum());
            int totalPageNum = totalCount % limitHelper.getPageSize() == 0 ? totalCount / limitHelper.getPageSize() : totalCount / limitHelper.getPageSize() + 1;
            pagination.setTotalPageNum(totalPageNum);
        }
        return pagination;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public String getCurrentUri() {
        return currentUri;
    }

    public void setCurrentUri(String currentUri) {
        this.currentUri = currentUri;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    public String getPreviousUri() {
        return previousUri;
    }

    public void setPreviousUri(String previousUri) {
        this.previousUri = previousUri;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}
