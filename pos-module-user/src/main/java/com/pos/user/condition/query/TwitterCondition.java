/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.condition.query;

import java.util.List;

/**
 * @author 睿智
 * @version 1.0, 2017/7/10
 */
public class TwitterCondition {

    private String twitter;

    private List<Long> twitterIds;

    private Long peId;

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public List<Long> getTwitterIds() {
        return twitterIds;
    }

    public void setTwitterIds(List<Long> twitterIds) {
        this.twitterIds = twitterIds;
    }

    public Long getPeId() {
        return peId;
    }

    public void setPeId(Long peId) {
        this.peId = peId;
    }
}
