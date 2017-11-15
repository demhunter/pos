/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.dto.platformEmployee;

import java.io.Serializable;


/**
 * @author lifei
 * @version 1.0, 2017/10/12
 */
public class TwitterChildDto implements Serializable {

    private Long twitterId; // 推客id

    private Byte childLevel; // 下级推客级别

    private Long childrenTwitterId; // 下级推客id

    public Long getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(Long twitterId) {
        this.twitterId = twitterId;
    }

    public Byte getChildLevel() {
        return childLevel;
    }

    public void setChildLevel(Byte childLevel) {
        this.childLevel = childLevel;
    }

    public Long getChildrenTwitterId() {
        return childrenTwitterId;
    }

    public void setChildrenTwitterId(Long childrenTwitterId) {
        this.childrenTwitterId = childrenTwitterId;
    }
}
