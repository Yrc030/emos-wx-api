package com.yrc.emos.wx;

import com.auth0.jwt.JWT;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Directory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;

/**
 * Description:
 * User: joker
 * Date: 2022-05-14-11:01
 * Time: 11:01
 */
@Slf4j
@SpringBootTest
public class JWTTest {

    @Autowired
    private JWTUtil jwtUtil;


    /**
     * 用于测试:
     */
    @Test
    public void test() throws InterruptedException {
        String token = jwtUtil.createToken(123);
        log.info("token = {}", token);
        Integer userId = jwtUtil.getUserId(token);
        log.info("userId = {}", userId);
        jwtUtil.verifyToken(token);

        SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = smdf.format(JWT.decode(token).getExpiresAt());
        System.out.println("date = " + date);

        Thread.sleep(3000);

        date = smdf.format(JWT.decode(token).getExpiresAt());
        System.out.println("date = " + date);

        String token1 = jwtUtil.createToken(123);
        log.info("token1 = {}", token1);
        Integer userId1 = jwtUtil.getUserId(token);
        log.info("userId1 = {}", userId1);
        jwtUtil.verifyToken(token1);
        System.out.println(("token == token1 = " + token).equals(token1));


        String date1 = smdf.format(JWT.decode(token).getExpiresAt());
        System.out.println("date1 = " + date1);
    }



}
