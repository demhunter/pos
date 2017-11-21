package com.pos.user.dto.platformEmployee;

import com.pos.common.util.validation.FieldChecker;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 家居顾问服务上限
 *
 * @author lifei
 * @version 1.0, 2017/10/12
 */
public class PreServiceNumDto implements Serializable {

    private int toplimit;

    public int getToplimit() {
        return toplimit;
    }

    public void setToplimit(int toplimit) {
        this.toplimit = toplimit;
    }

    /**
     * 检查字段完整性.
     */
    public void check() {
        FieldChecker.checkMinMaxValue2(new BigDecimal(toplimit), BigDecimal.ZERO, new BigDecimal(9999), "服务上限");
    }
}
