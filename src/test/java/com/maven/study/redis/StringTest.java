package com.maven.study.redis;

import com.maven.study.common.redis.util.RedisStringsBitMapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @program: study
 * @description: redis String测试类
 * @author: Ding XZ
 * @date: 2021-07-28 11:26
 */

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StringTest {

//    @Resource
//    RedisTemplate<String, String> redisTemplate;

    @Resource
    RedisStringsBitMapUtil redisStringsBitMapUtil;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void context(){
        String userId = "zhangsan";
        long day1 = 360;
        long day2 = 365;
        long day3 = 60;
//        // 创建模拟数据
//        redisStringsUtil.setBitMap(userId, day1, true);
//        redisStringsUtil.setBitMap(userId, day2, true);
//        redisStringsUtil.setBitMap(userId, day3, true);
//
//        // 获取具体日期的状态
//        System.out.println(redisStringsUtil.getBitMap(userId, day1));
//        System.out.println(redisStringsUtil.getBitMap(userId, day2));
//        System.out.println(redisStringsUtil.getBitMap(userId, day3));


        // 统计二进制位为1的个数
        // redisStringsUtil.getBitCount(userId + "123456");

        // 统计一定范围内，二进制位1的个数
//        redisStringsUtil.getBitCount(userId, -1, -2);

//        // 查询，从左到右查询第一个true（1）出现的位置，并且返回相应位置
//        redisStringsBitMapUtil.getBitPos(userId, true);
//
//        // 查询，在一定范围内从左到右查询第一个true（1）出现的位置，并且返回相应位置
//        redisStringsBitMapUtil.getBitPos(userId, true, -2, -1);

        byte[] zhangsanKey = userId.getBytes();
        byte[] lisiKey = "lisi".getBytes();
        redisStringsBitMapUtil.getBitOp(RedisStringCommands.BitOperation.AND, "newKey", zhangsanKey);

    }

    /**
     * @description: 创建数据，随机记录一些天数
     * @param: userId
     * @return: void
    */
    private void initData(String userId){
        boolean signIn = true;
        // 创建随机天数
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int day = random.nextInt(365);
            redisStringsBitMapUtil.setBitMap(userId, day, signIn);
        }
    }


}
