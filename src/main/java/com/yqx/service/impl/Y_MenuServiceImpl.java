package com.yqx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import com.yqx.dao.Y_MenuMapper;
import com.yqx.entity.Y_Menu;
import com.yqx.service.Y_MenuService;

@Service
public class Y_MenuServiceImpl extends BaseService<Y_Menu> implements Y_MenuService {
	
	@Autowired
	private Y_MenuMapper menuMapper;
	
	@Override
	public List<Y_Menu> getMenuByUserId(Integer userId) {
		
		return menuMapper.getMenuByUserId(userId);
	}
	
	@Override
	public List<Y_Menu> getMenuByRole(Integer roleId) {
		
		return menuMapper.getMenuByRole(roleId);
	}

	@Override
	public List<Y_Menu> selectByParentIdAndRoleId(Integer parentId,Integer roleId) {
		
		return menuMapper.selectByParentIdAndRoleId(parentId, roleId);
	}

	@Override
	@Transactional
	public void batchDelMenu(Y_Menu menu) {
		
		Example trolemenuexample = new Example(Y_Menu.class);
        trolemenuexample.or().andEqualTo("id",menu.getId());
        this.deleteByExample(trolemenuexample);
        //删除该节点的所有子节点
        Example tmenuexample = new Example(Y_Menu.class);
        tmenuexample.or().andEqualTo("p_id",menu.getId());
        this.deleteByExample(tmenuexample);
        //删除该节点
        this.delete(menu.getId());
	}
}
