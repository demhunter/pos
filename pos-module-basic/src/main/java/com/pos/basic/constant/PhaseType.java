/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.constant;

import com.pos.basic.enumHandler.CommonByteEnum;
import com.pos.common.util.exception.IllegalParamException;

import java.util.Objects;

/**
 * 阶段类型
 *
 * @author cc
 * @version 1.0, 2016/11/8
 */
public enum PhaseType implements CommonByteEnum {

    BOOKING_PHASE((byte) 1, "预约阶段", ""),

    CONTRACT_PHASE((byte) 2, "达成协议阶段", ""),

    CHECK_PHASE((byte) 100, "验收阶段", ""),

    CUSTOMIZED_PHASE_ONE((byte) 3, "阶段一期", "一期款"),

    CUSTOMIZED_PHASE_TWO((byte) 4, "阶段二期", "二期款"),

    CUSTOMIZED_PHASE_THREE((byte) 5, "阶段三期", "三期款"),

    CUSTOMIZED_PHASE_FOUR((byte) 6, "阶段四期", "四期款"),

    CUSTOMIZED_PHASE_FIVE((byte) 7, "阶段五期", "五期款"),

    CUSTOMIZED_PHASE_SIX((byte) 8, "阶段六期", "六期款"),

    CUSTOMIZED_PHASE_SEVEN((byte) 9, "阶段七期", "七期款"),

    CUSTOMIZED_PHASE_EIGHT((byte) 10, "阶段八期", "八期款"),

    CUSTOMIZED_PHASE_NINE((byte) 11, "阶段九期", "九期款"),

    CUSTOMIZED_PHASE_TEN((byte) 12, "阶段十期", "十期款"),

    CUSTOMIZED_PHASE_ELEVEN((byte) 13, "阶段十一期", "十一期款"),

    CUSTOMIZED_PHASE_TWELVE((byte) 14, "阶段十二期", "十二期款"),

    CUSTOMIZED_PHASE_THIRTEEN((byte) 15, "阶段十三期", "十三期款"),

    CUSTOMIZED_PHASE_FOURTEEN((byte) 16, "阶段十四期", "十四期款"),

    CUSTOMIZED_PHASE_FIFTEEN((byte) 17, "阶段十五期", "十五期款"),

    CUSTOMIZED_PHASE_SIXTEEN((byte) 18, "阶段十六期", "十六期款"),

    CUSTOMIZED_PHASE_SEVENTEEN((byte) 19, "阶段十七期", "十七期款"),

    CUSTOMIZED_PHASE_EIGHTEEN((byte) 20, "阶段十八期", "十八期款"),

    CUSTOMIZED_PHASE_NINETEEN((byte) 21, "阶段十九期", "十九期款"),

    CUSTOMIZED_PHASE_TWENTY((byte) 22, "阶段二十期", "二十期款"),

    CUSTOMIZED_PHASE_TWENTY_ONE((byte) 23, "阶段二十一期", "二十一期款"),

    CUSTOMIZED_PHASE_TWENTY_TWO((byte) 24, "阶段二十二期", "二十二期款"),

    CUSTOMIZED_PHASE_TWENTY_THREE((byte) 25, "阶段二十三期", "二十三期款"),

    CUSTOMIZED_PHASE_TWENTY_FOUR((byte) 26, "阶段二十四期", "二十四期款"),

    CUSTOMIZED_PHASE_TWENTY_FIVE((byte) 27, "阶段二十五期", "二十五期款"),

    CUSTOMIZED_PHASE_TWENTY_SIX((byte) 28, "阶段二十六期", "二十六期款"),

    CUSTOMIZED_PHASE_TWENTY_SEVEN((byte) 29, "阶段二十七期", "二十七期款"),

    CUSTOMIZED_PHASE_TWENTY_EIGHT((byte) 30, "阶段二十八期", "二十八期款"),

    CUSTOMIZED_PHASE_TWENTY_NINE((byte) 31, "阶段二十九期", "二十九期款"),

    CUSTOMIZED_PHASE_THIRTY((byte) 32, "阶段三十期", "三十期款"),

    CUSTOMIZED_PHASE_THIRTY_ONE((byte) 33, "阶段三十一期", "三十一期款"),

    CUSTOMIZED_PHASE_THIRTY_TWO((byte) 34, "阶段三十二期", "三十二期款"),

    CUSTOMIZED_PHASE_THIRTY_THREE((byte) 35, "阶段三十三期", "三十三期款"),

    CUSTOMIZED_PHASE_THIRTY_FOUR((byte) 36, "阶段三十四期", "三十四期款"),

    CUSTOMIZED_PHASE_THIRTY_FIVE((byte) 37, "阶段三十五期", "三十五期款"),

    CUSTOMIZED_PHASE_THIRTY_SIX((byte) 38, "阶段三十六期", "三十六期款"),

    CUSTOMIZED_PHASE_THIRTY_SEVEN((byte) 39, "阶段三十七期", "三十七期款"),

    CUSTOMIZED_PHASE_THIRTY_EIGHT((byte) 40, "阶段三十八期", "三十八期款"),

    CUSTOMIZED_PHASE_THIRTY_NINE((byte) 41, "阶段三十九期", "三十九期款"),

    CUSTOMIZED_PHASE_FORTY((byte) 42, "阶段四十期", "四十期款"),

    CUSTOMIZED_PHASE_FORTY_ONE((byte) 43, "阶段四十一期", "四十一期款"),

    CUSTOMIZED_PHASE_FORTY_TWO((byte) 44, "阶段四十二期", "四十二期款"),

    CUSTOMIZED_PHASE_FORTY_THREE((byte) 45, "阶段四十三期", "四十三期款"),

    CUSTOMIZED_PHASE_FORTY_FOUR((byte) 46, "阶段四十四期", "四十四期款"),

    CUSTOMIZED_PHASE_FORTY_FIVE((byte) 47, "阶段四十五期", "四十五期款"),

    CUSTOMIZED_PHASE_FORTY_SIX((byte) 48, "阶段四十六期", "四十六期款"),

    CUSTOMIZED_PHASE_FORTY_SEVEN((byte) 49, "阶段四十七期", "四十七期款"),

    CUSTOMIZED_PHASE_FORTY_EIGHT((byte) 50, "阶段四十八期", "四十八期款"),

    CUSTOMIZED_PHASE_FORTY_NINE((byte) 51, "阶段四十九期", "四十九期款"),

    CUSTOMIZED_PHASE_FIFTY((byte) 52, "阶段五十期", "五十期款");

    /**
     * 标识码
     */
    private final byte code;

    /**
     * 描述内容
     */
    private final String desc;

    private final String value;

    PhaseType(byte code, String desc, String value) {
        this.code = code;
        this.desc = desc;
        this.value = value;
    }

    public static PhaseType getEnum(byte code) {
        for (PhaseType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }

        throw new IllegalParamException("非法阶段类型code值");
    }

    public static PhaseType getNextPhase(PhaseType currentPhase) {
        if (currentPhase.equals(BOOKING_PHASE)
                || currentPhase.equals(CONTRACT_PHASE)
                || currentPhase.equals(CHECK_PHASE)) {
            throw new IllegalParamException("非法的阶段类型");
        }

        return getEnum((byte) (currentPhase.getCode() + 1));
    }

    public boolean canRemove() {
        return this.code != BOOKING_PHASE.getCode() && this.code != CONTRACT_PHASE.getCode() && this.code != CHECK_PHASE.getCode();
    }

    public boolean canCancel() {
        return this.code != BOOKING_PHASE.getCode() && this.code != CONTRACT_PHASE.getCode() && this.code != CHECK_PHASE.getCode();
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() { return value; }
}
