package com.maven.study.common.redis.common.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @program: study
 * @description: Redis配置文件
 * @author: DXZ
 * @date: 2021-07-28 10:40
 */
@Configuration
/**
 * @description: 启用缓存，这个注解很重要；
*/
@EnableCaching
public class RedisLettuceConfig {

    @Value("${spring.redis.lettuce.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle}")
    private int redisPoolMinIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private Long redisPoolMaxWait;
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private Long timeout;
    @Value("${spring.redis.lettuce.shutdown-timeout}")
    private Long shutdownTimeOut;

    @Value("${redis.cache.database}")
    private int cacheDatabaseIndex;


//    /**
//     *  配置 cache RedisTemplate
//     * @return  RedisTemplate<String,Serializable>r
//     */
//    @Bean(value="cacheRedisTemplate")
//    public RedisTemplate<String,Object> getCacheRedisTemplate(){
//        return this.initRedisTemplate();
//    }

//    /**
//     *  配置Session RedisTemplate
//     * @return  RedisTemplate<String,Serializable>r
//     */
//    @Bean(value="sessionRedisTemplate")
//    public RedisTemplate<String,Object> getSessionRedisTemplate(){
//        return this.initRedisTemplate();
//    }

    /**
     * @description: 自定义LettuceConnectionFactory,这一步的作用就是返回根据你传入参数而配置的
     * LettuceConnectionFactory，
     * 也可以说是LettuceConnectionFactory的原理了，
     * 后面我会详细讲解的,各位同学也可先自己看看源码
     * 这里定义的方法 createLettuceConnectionFactory，方便快速使用
     * @param: dbIndex
     * @param: hostName
     * @param: port
     * @param: password
     * @param: maxIdle
     * @param: minIdle
     * @param: maxActive
     * @param: maxWait
     * @param: timeOut
     * @param: shutdownTimeOut
     * @return: org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
    */
    private  LettuceConnectionFactory createLettuceConnectionFactory(
            int dbIndex, String hostName, int port,
            String username, String password,
            int maxIdle,int minIdle,int maxActive,
            Long maxWait, Long timeOut,Long shutdownTimeOut){

        //redis配置
        RedisConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(hostName,port);
        ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
        ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

        //Lettuce 连接池客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(timeOut));

        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new
                LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
        lettuceConnectionFactory .afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    /**
     * @description: 得到StringRedisTemplate 区别于RedisTemplate key和value都为string
     * @param: redisConnectionFactory
     * @return: org.springframework.data.redis.core.StringRedisTemplate
    */
    @Bean
    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return stringRedisTemplate;
    }

    /**
     * @description: 配置-序列化
     * @param: redisConnectionFactory
     * @return: org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
    */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //定义key序列化方式
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        //Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
        template.setKeySerializer(redisSerializer);
        //定义value的序列化方式
//        template.setValueSerializer(getGenericJackson2JsonRedisSerializer());
//        template.setHashValueSerializer(getGenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * @description: 反序列化
     * @param: redisConnectionFactory
     * @return: org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
    */
    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(RedisSerializer.json());
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

//    /**
//     * @description: 序列化方式
//     * 存储序列化方式GenericJackson2JsonRedisSerializer 和Jackson2JsonRedisSerializer 前者加入了@class包属性
//     * @param:
//     * @return: org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
//    */
//    @Bean(name = "springSessionDefaultRedisSerializer")
//    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
//        return new GenericJackson2JsonRedisSerializer();
//    }

    public RedisTemplate<String,Object> initRedisTemplate(){
        // 创建客户端连接
        LettuceConnectionFactory lettuceConnectionFactory =
                createLettuceConnectionFactory
                        (cacheDatabaseIndex, host, port, "", password, redisPoolMaxIdle, redisPoolMinIdle,
                                redisPoolMaxActive, redisPoolMaxWait, timeout, shutdownTimeOut);
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 使用 String 作为 Key 的序列化器,使用 Jackson 作为 Value 的序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
