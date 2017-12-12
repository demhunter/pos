/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.dto.phoneAddress;

import java.io.Serializable;

/**
 * 阿里云的云市场-手机号码归属地查询结果DTO
 *
 * @author wangbing
 * @version 1.0, 2017/7/12
 */
public class PhoneAddressResultDto implements Serializable {

    private static final long serialVersionUID = -2992643063201929985L;
    private Integer showapi_res_code; // 返回码，0为成功，其他失败。失败时不扣点数

    private String showapi_res_error; // 错误信息

    private PhoneAddressResultBody showapi_res_body; // 手机号码归属地信息

    public PhoneAddressResultBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(PhoneAddressResultBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public Integer getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(Integer showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    /**
     * 手机号码归属地信息
     */
    public class PhoneAddressResultBody implements Serializable {

        Integer ret_code; // 0为成功，其他失败。失败时不扣点数

        String prov; // 省份名称，ps：四川

        String city; // 城市名称，ps：成都市

        Integer num; // 号段，ps：1832872

        String cityCode; // 城市编号，ps：510100

        String areaCode; // 区号，ps：028

        Integer type; // 1移动    2电信    3联通

        String name; // 运营商名称，ps：移动183卡

        String postCode; // 邮编，ps：610000

        String provCode; // 此地区身份证号开头几位，ps：510000

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public Integer getRet_code() {
            return ret_code;
        }

        public void setRet_code(Integer ret_code) {
            this.ret_code = ret_code;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPostCode() {
            return postCode;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public String getProvCode() {
            return provCode;
        }

        public void setProvCode(String provCode) {
            this.provCode = provCode;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
