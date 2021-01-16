package com.yqx.dao;

import org.apache.ibatis.annotations.Mapper;

import com.yqx.entity.Y_User;
import com.yqx.util.MyMapper;

@Mapper
public interface Y_UserMapper extends MyMapper<Y_User>{

    // 查询用户信息
    Y_User findByName(String name);

   	// 查询用户信息、角色、权限
    Y_User findById(String id);
}