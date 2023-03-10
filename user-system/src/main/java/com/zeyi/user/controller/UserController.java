package com.zeyi.user.controller;

import com.alibaba.fastjson2.JSONObject;
import com.zeyi.user.entity.DataTable;
import com.zeyi.user.entity.MsgInfo;
import com.zeyi.user.entity.UserInfo;
import com.zeyi.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户管理-控制层
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 查询用户信息
     *
     * @param userInfo    用户关键字信息
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param orderBy     排序
     * @return 用户信息
     */
    @PostMapping(value = "queryUser")
    public DataTable queryUser(UserInfo userInfo, Integer currentPage, Integer pageSize, String orderBy) {
        return userService.queryUser(userInfo, currentPage, pageSize, orderBy);
    }

    /**
     * 新增用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    @PostMapping(value = "/insertUser")
    public MsgInfo insertUser(UserInfo userInfo) {
        LOGGER.info("开始新增User，用户信息{}", JSONObject.toJSONString(userInfo));

        return userService.insertUser(userInfo);
    }

    /**
     * 更新用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    @PostMapping(value = "/updateUser")
    public MsgInfo updateUser(UserInfo userInfo) {
        LOGGER.info("开始更新User，用户ID：{}", userInfo.getId());

        return userService.updateUser(userInfo);
    }

    /**
     * 删除用户、单一或批量-逻辑删除
     *
     * @param idList 用户id
     * @return 返回信息
     */
    @DeleteMapping(value = "/deleteUser")
    public MsgInfo deleteUser(String idList) {
        LOGGER.info("开始删除User，用户ID：{}", JSONObject.toJSONString(idList));

        List<Integer> ids = new ArrayList<>();
        String[] strArray = idList.split(",");
        List<String> strList = Arrays.asList(strArray);
        for (String str : strList) {
            ids.add(Integer.parseInt(str));
        }
        return userService.deleteUser(ids);
    }

    /**
     * 撤销操作
     *
     * @return 返回信息
     */
    @PostMapping(value = "/undo")
    public MsgInfo undo() {
        LOGGER.info("开始撤销操作");

        return userService.undo();
    }
}
