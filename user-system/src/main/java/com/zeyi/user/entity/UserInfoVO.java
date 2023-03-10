package com.zeyi.user.entity;

import lombok.Data;

/**
 * 页面显示用户信息
 *
 * @author sujiahua
 * @date 2023/3/10
 */
@Data
public class UserInfoVO {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 电话
     */
    private String phone;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private String createTime;
}
