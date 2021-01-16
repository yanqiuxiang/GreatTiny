package com.yqx.service;

import java.util.List;

import com.yqx.entity.Y_Menu;

public interface Y_MenuService extends IService<Y_Menu>{
	
	
	public List<Y_Menu> getMenuByUserId(Integer userId);
	
	public List<Y_Menu> getMenuByRole(Integer roleId) ;
	
	public List<Y_Menu> selectByParentIdAndRoleId(Integer parentId, Integer roleId);
	
	public void batchDelMenu(Y_Menu menu);
}
