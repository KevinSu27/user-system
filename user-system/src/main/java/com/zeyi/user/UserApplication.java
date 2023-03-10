package com.zeyi.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户系统 启动类
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@SpringBootApplication
@MapperScan("com.zeyi.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
