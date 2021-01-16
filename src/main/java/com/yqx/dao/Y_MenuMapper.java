package com.yqx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yqx.entity.Y_Menu;
import com.yqx.util.MyMapper;

@Mapper
public interface Y_MenuMapper extends MyMapper<Y_Menu>{
	
	public List<Y_Menu> getMenuByUserId(Integer userId);
	
	public List<Y_Menu> getMenuByRole(Integer roleId);
	
	public List<Y_Menu> selectByParentIdAndRoleId(@Param("parentId")Integer parentId,@Param("roleId")Integer roleId);
}
