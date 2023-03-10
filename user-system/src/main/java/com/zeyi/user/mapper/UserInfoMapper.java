package com.zeyi.user.mapper;


import com.zeyi.user.entity.UserInfo;
import com.zeyi.user.entity.UserInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息-Mapper
 *
 * @author sujiahua
 * @date 2023/3/9
 */
public interface UserInfoMapper {
    long countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByPhone(String phone);

    int deleteByIdList(List<Integer> idList);

    int updateByIdList(List<Integer> idList);
}