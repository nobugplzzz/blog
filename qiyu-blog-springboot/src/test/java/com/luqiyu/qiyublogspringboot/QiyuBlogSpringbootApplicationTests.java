package com.luqiyu.qiyublogspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;

@SpringBootTest
class QiyuBlogSpringbootApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("testKey","testValue");
        System.out.println(redisTemplate.opsForValue().get("testKey"));
    }

}
