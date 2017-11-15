/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/10/24
 */
public class QueryTeamMsgRequestDto {

    private String tid;//	群id
    private String accid;//查询用户对应的accid.
    private String begintime;//	开始时间，ms
    private String endtime;//截止时间，ms
    private int limit;//本次查询的消息条数上限(最多100条),小于等于0，或者大于100，会提示参数错误
    private int reverse;//1按时间正序排列，2按时间降序排列。其它返回参数414错误。默认是按降序排列

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getReverse() {
        return reverse;
    }

    public void setReverse(int reverse) {
        this.reverse = reverse;
    }
}
