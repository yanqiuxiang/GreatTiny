package com.yqx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.yqx.dao.Y_UserMapper;
import com.yqx.dao.Y_UserRoleMapper;
import com.yqx.entity.Y_User;
import com.yqx.entity.Y_UserRole;
import com.yqx.service.Y_UserService;

@Service
public class Y_UserServiceImpl extends BaseService<Y_User> implements Y_UserService{
	
	@Autowired
	private Y_UserMapper userMapper;
	
	@Autowired
	private Y_UserRoleMapper userRoleService;
	
	
   // 查询用户信息
   @Override
   public Y_User findByName(String name){
	   
	   return userMapper.findByName(name);
   }

   	// 查询用户信息、角色、权限
   @Override
   public Y_User findById(String id) {
	   
	   return userMapper.findById(id);
   }

	@Override
	@Transactional
	public void batchDelUser(Y_User user) {
		
		 Example tuserroleexample = new Example(Y_UserRole.class);
         tuserroleexample.or().andEqualTo("user_id",user.getId());
         userRoleService.deleteByExample(tuserroleexample);
         this.delete(user.getId());
	}

	
	@Override
	@Transactional
	public void addUser(Integer[] role, Integer id) {
		Example tuserroleexample = new Example(Y_UserRole.class);
        tuserroleexample.or().andEqualTo("user_id",id);
        userRoleService.deleteByExample(tuserroleexample);
        if (role!=null && role.length>0) {
            for (Integer roleid : role) {
            	Y_UserRole tuserrole = new Y_UserRole();
                tuserrole.setRole_id(roleid);
                tuserrole.setUser_id(id);
                userRoleService.insert(tuserrole);
            }
        }
	}
}
