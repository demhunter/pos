/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.dto.message;

import com.pos.im.constant.InternalMessageType;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.pos.common.util.basic.PrintableBeanUtils;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.util.Date;

/**
 * IM站内消息记录的DTO.
 *
 * @author wayne
 * @version 1.0, 2016/11/7
 */
@ApiModel
public class InternalMessageDto implements Serializable {

    private static final long serialVersionUID = 3235804060395162781L;

    @ApiModelProperty("消息编号")
    private Long id;

    @ApiModelProperty("消息类型：1 = 普通消息，2 = 案例会话消息，3 = 预约订单消息，4 = 推客消息, 5 = 订单消息, 6 = 主材内购消息")
    private int type;

    @ApiModelProperty("消息子类型")
    private Integer subType;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("消息扩展信息")
    private String extendInfo;

    @ApiModelProperty("消息关联的目标ID，由type决定其指向意义，比如type=2，则targetId表示案例会话ID")
    private Long targetId;

    @ApiModelProperty("v1.6.0 * 参数列表，以英文逗号分隔多个参数，注意参数顺序（使用注意事项：要使用此字段，必须把tartgetId置为0）")
    private String parameters;

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @ApiModelProperty("消息发送时间")
    private Date sendTime;

    /**
     * 检查字段完整性.
     *
     * @param fieldPrefix 抛出异常时提示错误字段的前缀名, 可以为空
     * @throws ValidationEception 未设置不能为空的字段, 或者字段值不符合约定
     */
    @SuppressWarnings("all")
    public void check(String fieldPrefix) {
        fieldPrefix = fieldPrefix == null ? "" : fieldPrefix + ".";
        FieldChecker.checkEmpty(title, fieldPrefix + "title");
        FieldChecker.checkEmpty(content, fieldPrefix + "content");
        FieldChecker.checkEmpty(sendTime, fieldPrefix + "sendTime");
        InternalMessageType msgType = parseType();
        if (msgType == null) {
            throw new ValidationException("'" + fieldPrefix + "type'无效值: " + type);
        }
        if (msgType.isSubTypeRequired()) {
            if (subType == null) {
                throw new ValidationException(msgType.name() + "类型的消息必须设置subType");
            }
            if (parseSubType() == null) {
                throw new ValidationException("'" + fieldPrefix + "subType'无效值: " + subType);
            }
        }
    }

    @Override
    public String toString() {
        return PrintableBeanUtils.toString(this);
    }

    public InternalMessageType parseType() {
        return InternalMessageType.getEnum((byte) type);
    }

    public InternalMessageType.SubType parseSubType() {
        return InternalMessageType.SubType.getEnum(subType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 调用此方法设置消息的查看类型：1 = 仅查看，2 = 跳转会话，3 = 跳转详情
     */
    public void parseInternalMessageViewType() {
        this.setType(parseType().getViewType());
    }

}