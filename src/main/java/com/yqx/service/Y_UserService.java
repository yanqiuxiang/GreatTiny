package com.yqx.service;

import com.yqx.entity.Y_User;


public interface Y_UserService extends IService<Y_User>{
	
	// 查询用户信息
   public Y_User findByName(String name);

   	// 查询用户信息、角色、权限
   public Y_User findById(String id);
	
   //删除用户及相关权限信息 
   public void batchDelUser(Y_User user); 
   
   //新增用户及其权限信息
   public void addUser(Integer[] role,Integer id);
}
