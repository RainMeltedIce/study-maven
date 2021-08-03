package com.maven.study.common.redis.modules.example;

import com.maven.study.common.redis.common.util.RedisStringsBitMapUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: study
 * @description:
 * @author: Ding XZ
 * @date: 2021-08-03 13:52
 */
@Component
public class RedisBitMapExample {

    @Resource
    private RedisStringsBitMapUtil redisStringsBitMapUtil;

    /**
     * @description: 记录zhangsan一年中的签到/登录状态。数据结构 key：用户id offset：天数 value：签到状态1
     * @param:
     * @return: void
    */
    public void template1(){
        String zhangsanKey = "zhangsan";
        long zhangsanDay1 = 1;
        long zhangsanDay2 = 5;
        long zhangsanDay3 = 365;

        // 制造假数据
        redisStringsBitMapUtil.setBitMap(zhangsanKey, zhangsanDay1,true);
        redisStringsBitMapUtil.setBitMap(zhangsanKey, zhangsanDay2,true);
        redisStringsBitMapUtil.setBitMap(zhangsanKey, zhangsanDay3,true);

        // 获取具体天数的签到状态
        long day1 = 1;
        Boolean dayStatus = redisStringsBitMapUtil.getBitMap(zhangsanKey, day1);
        System.out.println("第"+day1+"天，"+zhangsanKey+"签到状态为:" + dayStatus);

        // 获取用户一定范围内的签到总数
        long userSignInCount = redisStringsBitMapUtil.getBitCount(zhangsanKey);
        System.out.println(zhangsanKey+"签到天数为:" + userSignInCount);
        // 获取一定范围内，用户一定范围内的签到总数
        long countStart = 0;
        long countEnd = 1;
        long userSignInCount1 = redisStringsBitMapUtil.getBitCount(zhangsanKey, 0, 1);
        System.out.println(zhangsanKey + "在" + countStart + "至" + countEnd*8 + "天中，签到天数为:" + userSignInCount1);

        // 获取用户第一次签到的天数
        long userSignInDay = redisStringsBitMapUtil.getBitPos(zhangsanKey, true);
        System.out.println(zhangsanKey+"第一次签到日期为:" + userSignInDay);
        // 在一定范围内，获取用户第一次签到的天数
        long userSignInDay1 = redisStringsBitMapUtil.getBitPos(zhangsanKey, true, 0, 1);
        System.out.println(zhangsanKey + "在" + countStart + "至" + countEnd*8 + "天中，首次签到日期为:" + userSignInDay1);

    }

    /**
     * @description: 获取某一天，所有用户的签到的状态 key：日期 offset：用户id value：数据
     * @param:
     * @return: void
    */
    public void template2(){
        long zhangsanId = 1001;
        long lisiId = 2001;
        String key1 = "20210803";

        // 模拟数据
        redisStringsBitMapUtil.setBitMap(key1, zhangsanId, true);
        redisStringsBitMapUtil.setBitMap(key1, lisiId, true);

        // 统计当天签到人数
        long count1 = redisStringsBitMapUtil.getBitCount(key1);
        System.out.println(key1 + "的签到人数为：" + count1);

        // 使用逻辑或运算，得到一定范围内签到的用户有多少（去重，即一人签到多天，只统计一次）
        // 模拟假数据 zhangsan 20210804 签到; wangwu 20210804 签到
        long wangwuId = 3001;
        String key2 = "20210804";
        redisStringsBitMapUtil.setBitMap(key2, zhangsanId, true);
        redisStringsBitMapUtil.setBitMap(key2, wangwuId, true);
        // 执行或运算，得到20210803和20210804两天的签到的状态
        String newKey = "newKey";
        redisStringsBitMapUtil.getBitOp(RedisStringCommands.BitOperation.OR, newKey, key1.getBytes(), key2.getBytes());
        // 获取20210803和20210804签到的总人数（去重）
        long count = redisStringsBitMapUtil.getBitCount(newKey);
        System.out.println("两天内的签到人数为：" + count);
    }

}
