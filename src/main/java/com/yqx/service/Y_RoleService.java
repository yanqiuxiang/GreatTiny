package com.yqx.service;

import java.util.List;

import com.yqx.entity.Y_Role;

public interface Y_RoleService extends IService<Y_Role>{
	
	
	public List<Y_Role> getRoleByUserId(Integer userId);
	
	public Y_Role getRoleById(Integer roleId);
	
	public void batchDelRole(Y_Role trole); 
	
	public void saveMenuSet(String menuIds, Integer roleId);
	
	public List<Y_Role> getNoneRoleById(Integer userId);
}
