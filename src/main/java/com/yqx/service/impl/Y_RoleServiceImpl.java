package com.yqx.service.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import com.yqx.dao.Y_MenuMapper;
import com.yqx.dao.Y_RoleMapper;
import com.yqx.dao.Y_RoleMenuMapper;
import com.yqx.dao.Y_UserRoleMapper;
import com.yqx.entity.Y_Menu;
import com.yqx.entity.Y_Role;
import com.yqx.entity.Y_RoleMenu;
import com.yqx.entity.Y_UserRole;
import com.yqx.service.Y_RoleService;

@Service
public class Y_RoleServiceImpl extends BaseService<Y_Role> implements Y_RoleService{
	
	@Autowired
	private Y_RoleMapper roleMapper;
	
	@Autowired
	private Y_UserRoleMapper userRoleService;
	
	@Autowired
	private Y_RoleMenuMapper  roleMenuService;
	
	@Autowired
	private Y_MenuMapper menuService;
	
	@Override
	public List<Y_Role> getRoleByUserId(Integer userId) {
		
		return roleMapper.getRoleByUserId(userId); 
	}
	@Override
	public Y_Role getRoleById(Integer roleId) {
		
		return roleMapper.getRoleById(roleId);
	}
	
	@Override
	@Transactional
	public void batchDelRole(Y_Role trole) {
		
		 Example tuserroleexample = new Example(Y_UserRole.class);
         tuserroleexample.or().andEqualTo("role_id",trole.getId());
         userRoleService.deleteByExample(tuserroleexample);
         Example trolemenuexample = new Example(Y_RoleMenu.class);
         trolemenuexample.or().andEqualTo("role_id",trole.getId());
         roleMenuService.deleteByExample(trolemenuexample);
         this.delete(trole.getId());
	}
	
	
	@Override
	@Transactional
	public void saveMenuSet(String menuIds, Integer roleId) {
		
		//先根据roleid查询出原有的对应的所有menuid集合
        List<Y_Menu> menuList = menuService.getMenuByRole(roleId);
        //移除所有没有pid的menuid
        Iterator<Y_Menu> it = menuList.iterator();
        while (it.hasNext()) {
        	Y_Menu tmenu = it.next();
            if (tmenu.getP_id() == null) {
                it.remove();
            }
        }
        List<Integer> menuIdList = new LinkedList<Integer>();
        for (Y_Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }

        if(menuIdList != null && menuIdList.size() > 0) {
            Example trolemenuExample = new Example(Y_RoleMenu.class);
            trolemenuExample.or().andEqualTo("role_id",roleId).andIn("menu_id",menuIdList);
            roleMenuService.deleteByExample(trolemenuExample);
        }

        String idsStr[] = menuIds.split(",");
        for (int i = 0,j = idsStr.length; i < j; i++) { // 然后添加所有角色权限关联实体
        	Y_RoleMenu trolemenu = new Y_RoleMenu();
            trolemenu.setRole_id(roleId);
            trolemenu.setMenu_id(Integer.parseInt(idsStr[i]));
            roleMenuService.insert(trolemenu);
        }
	}
	@Override
	public List<Y_Role> getNoneRoleById(Integer userId) {

		return roleMapper.getNoneRoleById(userId);
	}
	
}
