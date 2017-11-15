/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.mvc.support;

import com.pos.common.util.basic.PrintableBeanUtils;

import java.io.Serializable;

/**
 * 分页参数助手类
 * pageNum和pageSize均是从1开始计算
 *
 * Created by cc on 16/6/7.
 */
public class LimitHelper implements Serializable {

    private static final long serialVersionUID = -6396475463541222152L;

    /**
     * 每页最多显示两百条
     */
    public static final int MAX_PAGE_SIZE = 200;

    /**
     * 页号
     */
    private int pageNum;

    /**
     * 每页数目
     */
    private int pageSize;

    /**
     * 偏移量
     */
    private int offset;

    public LimitHelper(int pageNum, int pageSize) {
        this(pageNum, pageSize, true);
    }

    public LimitHelper(int pageNum, int pageSize, boolean isLimitMaxPageSize) {
        if (pageNum >= 1 && pageSize >= 1) {
            this.pageNum = pageNum;
            this.pageSize = isLimitMaxPageSize && pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
            this.offset = (pageNum - 1) * this.pageSize;
        } else {
            throw new IllegalArgumentException("分页参数错误! pageNum: " + pageNum + ", pageSize:" + pageSize);
        }
    }

    public static LimitHelper create(int pageNum, int pageSize) {
        return new LimitHelper(pageNum, pageSize);
    }

    public static LimitHelper create(int pageNum, int pageSize, boolean isLimitMaxPageSize) {
        return new LimitHelper(pageNum, pageSize, isLimitMaxPageSize);
    }

    public int getPageCount(int totalCount) {
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String toString() {
        return PrintableBeanUtils.toString(this);
    }
}