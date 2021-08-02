package com.maven.study.common.redis.modules.model;

/**
 * @program: study
 * @description: 地址模型
 * @author: Ding XZ
 * @date: 2021-08-02 16:20
 */
public class RedisAddress {

    /**
     *  省份
     */
    private String provinces;

    /**
     * 市区
    */
    private String city;

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
