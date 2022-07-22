package com.yrc.emos.wx;

import cn.hutool.core.util.RandomUtil;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.mapper.UserMapper;
import com.yrc.emos.wx.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-06-09-11:42
 * Time: 11:42
 */
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    private String[] photo = null;
    {
        photo = new String[17];
        for(int i = 1; i <= 17; i++) {
            photo[i - 1] = "https://emos-static.oss-cn-hangzhou.aliyuncs.com/img/header/header-" + (i < 10 ? "0" + i : i) +".jpg";
        }
    }

    /**
     * 用于测试:
     */
    @Test
    public void testGetUserById(){
        UserEntity user = userService.getUserById(335);
        System.out.println("user = " + user);
    }


    /**
     * 用于测试:
     */
    @Test
    public void test() {

        List<UserEntity> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            UserEntity user = new UserEntity();
            user.setName(RandomValueUtil.getChineseName());
            user.setSex(RandomValueUtil.name_sex);
            user.setDeptId(RandomUtil.randomInt(6));
            user.setPhoto(photo[RandomUtil.randomInt(17)]);
            user.setRole("[" + RandomUtil.randomInt(1, 4) + "]");
            user.setRoot(false);
            user.setStatus(1);
            user.setTel(RandomValueUtil.getTelephone());
            user.setEmail(RandomValueUtil.getEmail(10,20));
            LocalDateTime createTime = RandomUtil.randomDay(0, 365).toLocalDateTime();
            user.setCreateTime(createTime);
            user.setHiredate(createTime.toLocalDate());
            users.add(user);
        }

        //System.out.println("users = " + users);
        userService.saveBatch(users);
    }


    @Autowired
    private UserMapper userMapper;

    /**
     * 用于测试:
     */
    @Test
    public void testSelectUserInfo(){
        UserEntity userEntity = userMapper.selectUserInfo(18);
        System.out.println("userEntity = " + userEntity);
    }

}
