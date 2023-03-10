package com.zeyi.user.service;

import com.zeyi.user.entity.DataTable;
import com.zeyi.user.entity.MsgInfo;
import com.zeyi.user.entity.UserInfo;

import java.util.List;

/**
 * 用户管理-接口
 *
 * @author sujiahua
 * @date 2023/3/9
 */
public interface UserService {

    /**
     * 查询用户信息
     *
     * @param userInfo    用户关键字信息
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param orderBy     排序
     * @return 用户信息
     */
    DataTable queryUser(UserInfo userInfo, Integer currentPage, Integer pageSize, String orderBy);

    /**
     * 新增用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    MsgInfo insertUser(UserInfo userInfo);

    /**
     * 更新用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    MsgInfo updateUser(UserInfo userInfo);

    /**
     * 删除用户、单一或批量-逻辑删除
     *
     * @param idList 用户id
     * @return 返回信息
     */
    MsgInfo deleteUser(List<Integer> idList);

    /**
     * 撤销操作
     *
     * @return 返回信息
     */
    MsgInfo undo();
}
