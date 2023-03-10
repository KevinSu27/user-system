package com.zeyi.user.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zeyi.user.entity.*;
import com.zeyi.user.mapper.UserInfoMapper;
import com.zeyi.user.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户管理-实现
 *
 * @author sujiahua
 * @date 2023/3/9
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 正常用户(未删除)
     */
    private static final String NORMAL_USER = "0";

    /**
     * 手机号正则表达式
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(1[3-9]\\d{9}$)");
    /**
     * 手机号码长度
     */
    private static final int PHONE_LEN = 11;
    /**
     * 删除操作
     */
    private static final String OPERA_DELETE = "delete";

    /**
     * 用户操作记录
     */
    private Stack<OperationRecord> operationRecordStack = new Stack<>();

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息
     *
     * @param userInfo    用户关键字信息
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param orderBy     排序
     * @return 用户信息
     */
    @Override
    public DataTable queryUser(UserInfo userInfo, Integer currentPage, Integer pageSize, String orderBy) {
        DataTable data = new DataTable();

        // 排序，默认按创建时间倒序
        Page<?> page = PageHelper.startPage(currentPage, pageSize, "create_time desc");

        // 页面关键字搜索(如果有)
        List<UserInfo> userInfoList = queryByUserInfo(userInfo);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (UserInfo user : userInfoList) {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            String date = dateFormat.format(user.getCreateTime());
            userInfoVO.setCreateTime(date);
            userInfoVOList.add(userInfoVO);
        }

        data.setData(userInfoVOList);
        data.setTotal((page.getTotal()));
        return data;
    }

    /**
     * 新增用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    @Override
    public MsgInfo insertUser(UserInfo userInfo) {
        MsgInfo msgInfo = new MsgInfo();

        // 参数校验
        boolean isError = checkRequestParam(userInfo, msgInfo);
        if (isError) {
            return msgInfo;
        }

        try {
            // 先查询是否有该用户存在
            UserInfo user = userInfoMapper.selectByPhone(userInfo.getPhone());
            if (ObjectUtils.isEmpty(user)) {
                userInfo.setDelFlag(NORMAL_USER);
                userInfo.setCreateTime(new Date());
                userInfoMapper.insert(userInfo);
                LOGGER.info("新增User成功！");
            } else {
                userInfo.setId(user.getId());
                userInfo.setDelFlag(NORMAL_USER);
                userInfo.setUpdateTime(new Date());
                userInfoMapper.updateByPrimaryKey(userInfo);
                LOGGER.info("用户已存在，更新User成功！");
            }
            msgInfo.setCode(200);
            msgInfo.setMassage(MsgInfo.MSG_SUCCESS);
        } catch (Exception e) {
            msgInfo.setCode(500);
            msgInfo.setMassage(MsgInfo.MSG_FIALURE);
            LOGGER.error("新增User失败", e);
        }

        return msgInfo;
    }

    /**
     * 更新用户
     *
     * @param userInfo 用户信息
     * @return 返回信息
     */
    @Override
    public MsgInfo updateUser(UserInfo userInfo) {
        MsgInfo msgInfo = new MsgInfo();

        // 参数校验
        boolean isError = checkRequestParam(userInfo, msgInfo);
        if (isError) {
            return msgInfo;
        }

        try {
            // 更新用户
            userInfo.setUpdateTime(new Date());
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            LOGGER.info("用户ID：{} 更新User成功！", userInfo.getId());
            msgInfo.setCode(200);
            msgInfo.setMassage(MsgInfo.MSG_SUCCESS);
        } catch (Exception e) {
            msgInfo.setCode(500);
            msgInfo.setMassage(MsgInfo.MSG_FIALURE);
            LOGGER.error("更新User失败", e);
        }

        return msgInfo;
    }

    /**
     * 删除用户、单一或批量-逻辑删除
     *
     * @param idList 用户id
     * @return 返回信息
     */
    @Override
    public MsgInfo deleteUser(List<Integer> idList) {
        MsgInfo msgInfo = new MsgInfo();
        if (CollectionUtils.isEmpty(idList)) {
            msgInfo.setCode(408);
            msgInfo.setMassage("用户信息有误");
            LOGGER.warn("删除User失败：用户信息有误");
            return msgInfo;
        }

        try {
            // 删除用户-更新
            userInfoMapper.deleteByIdList(idList);
            LOGGER.info("用户ID：{} 删除成功！", JSONObject.toJSONString(idList));
            msgInfo.setCode(200);
            msgInfo.setMassage(MsgInfo.MSG_SUCCESS);

            // 保存数据，撤销使用
            operationRecordStack.push(new OperationRecord(OPERA_DELETE, idList));
        } catch (Exception e) {
            msgInfo.setCode(500);
            msgInfo.setMassage(MsgInfo.MSG_FIALURE);
            LOGGER.error("删除User失败", e);
        }

        return msgInfo;
    }

    /**
     * 撤销操作
     *
     * @return 返回信息
     */
    @Override
    public MsgInfo undo() {
        MsgInfo msgInfo = new MsgInfo();
        if (operationRecordStack.empty()) {
            msgInfo.setCode(408);
            msgInfo.setMassage("无法撤销");
            LOGGER.warn("无法撤销：用户没有进行删除操作");
            return msgInfo;
        }

        OperationRecord operationRecord = operationRecordStack.pop();
        if (StringUtils.equals(operationRecord.getOperation(), OPERA_DELETE)) {
            List<Integer> idList = operationRecord.getIdList();
            // 撤销删除
            userInfoMapper.updateByIdList(idList);
            LOGGER.info("用户ID：{} 撤销删除成功！", JSONObject.toJSONString(idList));
            msgInfo.setCode(200);
            msgInfo.setMassage(MsgInfo.MSG_SUCCESS);
        }

        return msgInfo;
    }

    /**
     * 参数校验
     *
     * @param userInfo 用户信息
     * @param msgInfo  返回信息
     * @return 是否有误
     */
    private boolean checkRequestParam(UserInfo userInfo, MsgInfo msgInfo) {
        if (ObjectUtils.isEmpty(userInfo)) {
            msgInfo.setCode(408);
            msgInfo.setMassage("用户信息不能为空");
            return true;
        }

        if (StringUtils.isBlank(userInfo.getName())) {
            LOGGER.warn("用户姓名为空");
            msgInfo.setCode(408);
            msgInfo.setMassage("用户姓名为空");
            return true;
        }

        if (StringUtils.isBlank(userInfo.getSex())) {
            LOGGER.warn("用户性别为空");
            msgInfo.setCode(408);
            msgInfo.setMassage("用户性别为空");
            return true;
        }

        if (StringUtils.isBlank(userInfo.getPhone())) {
            LOGGER.warn("用户电话为空");
            msgInfo.setCode(408);
            msgInfo.setMassage("用户电话为空");
            return true;
        } else if (!checkPhone(userInfo.getPhone())) {
            LOGGER.warn("用户电话有误");
            msgInfo.setCode(408);
            msgInfo.setMassage("用户电话有误");
            return true;
        }

        return false;
    }

    /**
     * 页面关键字搜索
     *
     * @param userInfo 用户关键字信息
     * @return 用户信息
     */
    private List<UserInfo> queryByUserInfo(UserInfo userInfo) {
        UserInfoExample userInfoExample = new UserInfoExample();
        UserInfoExample.Criteria criteria = userInfoExample.createCriteria().andDelFlagEqualTo(NORMAL_USER);

        if (StringUtils.isNotBlank(userInfo.getName())) {
            criteria.andNameLike("%" + userInfo.getName() + "%");
        }

        if (ObjectUtils.isNotEmpty(userInfo.getAge())) {
            criteria.andAgeEqualTo(userInfo.getAge());
        }

        if (StringUtils.isNotBlank(userInfo.getSex())) {
            criteria.andSexEqualTo(userInfo.getSex());
        }

        if (StringUtils.isNotBlank(userInfo.getPhone())) {
            criteria.andPhoneLike("%" + userInfo.getPhone() + "%");
        }

        if (StringUtils.isNotBlank(userInfo.getAddress())) {
            criteria.andAddressLike("%" + userInfo.getAddress() + "%");
        }

        return userInfoMapper.selectByExample(userInfoExample);
    }

    /**
     * 检查手机
     *
     * @param phone 手机
     * @return 是否是手机号码
     */
    public boolean checkPhone(String phone) {
        if (phone.length() == PHONE_LEN) {
            Matcher matcher = PHONE_PATTERN.matcher(phone);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
}


