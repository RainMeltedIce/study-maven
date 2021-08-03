common:
    config:
        RedisLettuceConfig:redis配置文件
    util:
        RedisStringsBitMapUtil: bitmap相关操作指令
modules：
    model： 实体类目录，暂时无用，计划测试不同序列化方式
        RedisAddress:地址实体类
        RedisSchool:学校实体类
    example:   例子
        RedisBitMapExample: bitmap实例