/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.message;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class VoiceMsg {

    private int dur;//语音持续时长ms
    private String md5;//md5
    private String url;//生成的url
    private String ext;//语音消息格式，只能是aac格式
    private int size;//语音文件大小

    public int getDur() {
        return dur;
    }

    public void setDur(int dur) {
        this.dur = dur;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
