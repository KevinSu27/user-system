package com.zeyi.user.service;

import com.zeyi.user.entity.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户管理-接口测试
 *
 * @author sujiahua
 * @date 2023/3/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testInsertUser() {
        for (int i = 10; i < 27; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName("小明" + (i - 9));
            userInfo.setAge(i + 10);
            userInfo.setPhone("134111122" + i);

            userInfo.setSex("0");
            userInfo.setAddress("广东省广州市番禺区" + i + "街" + 10 + "号");
            userInfo.setCreateTime(new Date());
            userService.insertUser(userInfo);
        }
    }
}
