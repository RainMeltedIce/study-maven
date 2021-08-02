package com.maven.study.common.redis.modules.model;

/**
 * @program: study
 * @description: 学校模型
 * @author: Ding XZ
 * @date: 2021-08-02 16:19
 */
public class RedisSchool {

    /**
     * 名称
    */
    private String name;

    /**
     * 地址
    */
    private RedisAddress address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RedisAddress getAddress() {
        return address;
    }

    public void setAddress(RedisAddress address) {
        this.address = address;
    }
}
