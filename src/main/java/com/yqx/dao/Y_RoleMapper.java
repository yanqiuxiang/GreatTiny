package com.yqx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yqx.entity.Y_Role;
import com.yqx.util.MyMapper;

@Mapper
public interface Y_RoleMapper extends MyMapper<Y_Role>{

	public List<Y_Role> getRoleByUserId(Integer userId);
	
	public Y_Role getRoleById(Integer roleId);
	
	//获取该用户没有的角色权限
	public List<Y_Role> getNoneRoleById(Integer userId);
}
