/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.message;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class PicMsg {

    private String name;//图片name
    private String md5;//md5
    private String url;//http://nimtest.nos.netease.com/cbc500e8-e19c-4b0f-834b-c32d4dc1075e",    //生成的url
    private String ext;//图片后缀
    private int w;//宽
    private int h;//高
    private int size;//图片大小

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
