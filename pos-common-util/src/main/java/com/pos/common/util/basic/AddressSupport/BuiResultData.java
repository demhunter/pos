/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic.AddressSupport;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * @author lifei
 * @version 1.0, 2017/9/11
 */
public class BuiResultData implements Serializable {

    private String location;//地址

    private String titlecont;//查询方式

    private String origip;//IP

    private String origipquery;//查询条件

    private String showlamp;

    private String showLikeShare;

    private String shareImage;

    @JsonProperty("ExtendedLocation")
    private String ExtendedLocation;

    @JsonProperty("OriginQuery")
    private String OriginQuery;

    private String tplt;

    private String resourceid;

    private String fetchkey;

    private String appinfo;

    private String role_id;

    private String disp_type;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitlecont() {
        return titlecont;
    }

    public void setTitlecont(String titlecont) {
        this.titlecont = titlecont;
    }

    public String getOrigip() {
        return origip;
    }

    public void setOrigip(String origip) {
        this.origip = origip;
    }

    public String getOrigipquery() {
        return origipquery;
    }

    public void setOrigipquery(String origipquery) {
        this.origipquery = origipquery;
    }

    public String getShowlamp() {
        return showlamp;
    }

    public void setShowlamp(String showlamp) {
        this.showlamp = showlamp;
    }

    public String getShowLikeShare() {
        return showLikeShare;
    }

    public void setShowLikeShare(String showLikeShare) {
        this.showLikeShare = showLikeShare;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getExtendedLocation() {
        return ExtendedLocation;
    }

    public void setExtendedLocation(String extendedLocation) {
        ExtendedLocation = extendedLocation;
    }

    public String getOriginQuery() {
        return OriginQuery;
    }

    public void setOriginQuery(String originQuery) {
        OriginQuery = originQuery;
    }

    public String getTplt() {
        return tplt;
    }

    public void setTplt(String tplt) {
        this.tplt = tplt;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getFetchkey() {
        return fetchkey;
    }

    public void setFetchkey(String fetchkey) {
        this.fetchkey = fetchkey;
    }

    public String getAppinfo() {
        return appinfo;
    }

    public void setAppinfo(String appinfo) {
        this.appinfo = appinfo;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getDisp_type() {
        return disp_type;
    }

    public void setDisp_type(String disp_type) {
        this.disp_type = disp_type;
    }
}
