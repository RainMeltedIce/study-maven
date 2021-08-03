package com.maven.study.redis;

import com.maven.study.common.redis.modules.example.RedisBitMapExample;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @program: study
 * @description: redis String测试类
 * @author: Ding XZ
 * @date: 2021-07-28 11:26
 */

@SpringBootTest
public class StringTest {

    @Resource
    RedisBitMapExample redisBitMapExample;

    @Test
    void context(){
        // 记录用户每天的签到状态
        redisBitMapExample.template1();
        // 记录每天，签到用户的总数
        redisBitMapExample.template2();
    }

}
