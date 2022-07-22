package com.yrc.emos.wx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * Description:
 * User: joker
 * Date: 2022-07-14-8:59
 * Time: 8:59
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate template;

    /**
     * 用于测试:
     */
    @Test
    public void test(){
        Set<String> keys = template.keys("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9*");
        System.out.println("keys = " + keys);
        int size = keys.size();
        template.delete(keys);
    }
}
