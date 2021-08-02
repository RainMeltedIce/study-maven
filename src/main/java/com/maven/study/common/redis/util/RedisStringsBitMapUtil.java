package com.maven.study.common.redis.util;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: study
 * @description: Redis Strings类型工具类
 * @author: Ding XZ
 * @date: 2021-07-28 16:14
 */
@Component
public class RedisStringsBitMapUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @description:    在offset位置新增二进制信息（0或1）
     * @param: key      key
     * @param: offset   偏移量
     * @param: value    值
     * @return: java.lang.Boolean
    */
    public Boolean setBitMap(String key, long offset, boolean value){
        Boolean setStatus = false;
        try{
            setStatus = redisTemplate.opsForValue().setBit(key, offset, value);
        }catch (NullPointerException e){
            return false;
        }
        return setStatus;
    }

    /**
     * @description: 得到offset位置的boolean状态
     * @param: key  key
     * @param: offset  偏移量
     * @return: java.lang.Boolean
    */
    public Boolean getBitMap(String key, long offset){
        if (key == null){
            throw new NullPointerException();
        }
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * @description: 获取二进制位中1出现的次数
     * @param: key
     * @return: java.lang.Long
    */
    public Long getBitCount(String key){
        byte[] keyByte =  key.getBytes();
        // redisTemplate中没有bitcount操作，通过RedisCallback接口，直接执行redis指令
        Long count = (Long) redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitCount(keyByte));
        return count;
    }

    /**
     * @description: 在一定范围内，获取二进制位中1出现的次数。start end参数并不是值二进制位的为数，1个字节=8位
     * @param: key
     * @param: start 起始字节位
     * @param: end   截止字节位
     * @return: java.lang.Long
    */
    public Long getBitCount(String key, long start, long end){
        byte[] keyByte =  key.getBytes();
        Long count = (Long) redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitCount(keyByte, start , end));
        return count;
    }

    /**
     * @description: 从左至右查询第一个bit出现的位置
     * @param: key
     * @param: bit 0或1
     * @return: java.lang.Long
    */
    public Long getBitPos(String key, boolean bit){
        byte[] keyByte = key.getBytes();
        Long index = (Long) redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitPos(keyByte, bit));
        return index;
    }

    /**
     * @description: 在一定范围内，从左至右查询第一个bit出现的位置。 start和end并非二进制位
     * @param: key
     * @param: bit
     * @param: start  起始位置 字节位
     * @param: end 截止位置 字节位
     * @return: java.lang.Long
    */
    public Long getBitPos(String key, boolean bit, long start, long end){
        byte[] keyByte = key.getBytes();
        Long index = (Long) redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitPos(keyByte,bit,Range.of(Range.Bound.inclusive(start), Range.Bound.inclusive(end))));
        return index;
    }

    public Long getBitOp(RedisStringCommands.BitOperation bitOperation, String newKey, byte[]... keys){
        byte[] newKeyBytes = newKey.getBytes();
        Object object = redisTemplate.execute((RedisCallback<Object>) connection -> connection.bitOp(bitOperation, newKeyBytes, keys));
        System.out.println(object);
        return null;
    }

}
