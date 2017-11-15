/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease.dto.request;

/**
 * @author 睿智
 * @version 1.0, 2017/9/8
 */
public class SingleSendMsgRequestDto {

    private String from	;//必须 发送者accid，用户帐号，最大32字符，必须保证一个APP内唯一
    private int ope;//必须 0：点对点个人消息，1：群消息（高级群），其他返回414
    private String to;//必须 ope==0是表示accid即用户id，ope==1表示tid即群id
    private int type;//必须 0 表示文本消息,1 表示图片，2 表示语音，3 表示视频，4 表示地理位置信息，6 表示文件，100 自定义消息类型
    private String body;//必须 请参考下方消息示例说明中对应消息的body字段，最大长度5000字符，为一个JSON串
    private boolean antispam;//本消息是否需要过自定义反垃圾系统，true或false, 默认false

    /**
     antispamCustom示例：
     {"type":1,"data":"custom content"}

     字段说明：
     1. type: 1:文本，2：图片，3视频;
     2. data: 文本内容or图片地址or视频地址。
     **/
    private String antispamCustom;//自定义的反垃圾内容, JSON格式，长度限制同body字段，不能超过5000字符，

    /**
     * option中字段不填时表示默认值 ，option示例:
     {"push":false,"roam":true,"history":false,"sendersync":true,"route":false,"badge":false,"needPushNick":true}
     字段说明：
     1. roam: 该消息是否需要漫游，默认true（需要app开通漫游消息功能）； 
     2. history: 该消息是否存云端历史，默认true；
      3. sendersync: 该消息是否需要发送方多端同步，默认true；
      4. push: 该消息是否需要APNS推送或安卓系统通知栏推送，默认true；
      5. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能);
      6. badge:该消息是否需要计入到未读计数中，默认true;
     7. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认true;
     8. persistent: 是否需要存离线消息，不设置该参数时默认true。
     */
    private String option;//发消息时特殊指定的行为选项,JSON格式，可用于指定消息的漫游，存云端历史，发送方多端同步，推送，消息抄送等特殊行为;

    private String pushcontent;//ios推送内容，不超过150字符，option选项中允许推送（push=true），此字段可以指定推送内容
    private String payload;//ios 推送对应的payload,必须是JSON,不能超过2k字符
    private String ext;//开发者扩展字段，长度限制1024字符
    private String forcepushlist;//发送群消息时的强推（@操作）用户列表，格式为JSONArray，如["accid1","accid2"]。若forcepushall为true，则forcepushlist为除发送者外的所有有效群成员
    private String forcepushcontent;//发送群消息时，针对强推（@操作）列表forcepushlist中的用户，强制推送的内容
    private boolean forcepushall;//发送群消息时，强推（@操作）列表是否为群里除发送者外的所有有效成员，true或false，默认为false

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getOpe() {
        return ope;
    }

    public void setOpe(int ope) {
        this.ope = ope;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isAntispam() {
        return antispam;
    }

    public void setAntispam(boolean antispam) {
        this.antispam = antispam;
    }

    public String getAntispamCustom() {
        return antispamCustom;
    }

    public void setAntispamCustom(String antispamCustom) {
        this.antispamCustom = antispamCustom;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPushcontent() {
        return pushcontent;
    }

    public void setPushcontent(String pushcontent) {
        this.pushcontent = pushcontent;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getForcepushlist() {
        return forcepushlist;
    }

    public void setForcepushlist(String forcepushlist) {
        this.forcepushlist = forcepushlist;
    }

    public String getForcepushcontent() {
        return forcepushcontent;
    }

    public void setForcepushcontent(String forcepushcontent) {
        this.forcepushcontent = forcepushcontent;
    }

    public boolean isForcepushall() {
        return forcepushall;
    }

    public void setForcepushall(boolean forcepushall) {
        this.forcepushall = forcepushall;
    }
}
