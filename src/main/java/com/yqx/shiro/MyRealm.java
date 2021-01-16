package com.yqx.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.yqx.entity.Y_Menu;
import com.yqx.entity.Y_Role;
import com.yqx.entity.Y_User;
import com.yqx.service.Y_MenuService;
import com.yqx.service.Y_RoleService;
import com.yqx.service.Y_UserService;

/**
 * 自定义Realm
 * */
public class MyRealm  extends AuthorizingRealm{
	
	@Autowired
    private Y_UserService userService;
	
	@Autowired
	private Y_RoleService roleService;
	
	@Autowired
	private Y_MenuService menuService;
	
	/**
	 * 执行授权逻辑
	 * */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		Y_User user = (Y_User) subject.getPrincipals().getPrimaryPrincipal();
		setRole(user.getId(), info);
		setPermission(user.getId(), info);
		return info;
	}
	
	/**
	 * 执行认证逻辑
	 * */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken cationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) cationToken;
		Y_User user = userService.findByName(token.getUsername());
		Subject subject = SecurityUtils.getSubject();
		subject.isRemembered();
		if(null == user) {
			System.out.println("用户名不存在的啦");
			return null;
		}
		SimpleAuthenticationInfo si = new SimpleAuthenticationInfo(user,user.getPassword(),"xxx");//认证密码
		return si;
	}
	
	/**
	 * 增加角色权限
	 * */
	private void setRole(Integer userId, SimpleAuthorizationInfo info) {
		List<Y_Role> roleList =  roleService.getRoleByUserId(userId);
		Set<String> roles = new HashSet<>();
		for (Y_Role role : roleList) {
			roles.add(role.getName());
		}
		info.setRoles(roles);
	}
	/**
	 * 增加模块权限
	 * */
	private void setPermission(Integer userId, SimpleAuthorizationInfo info) {
		List<Y_Menu> menuList = menuService.getMenuByUserId(userId);
		for (Y_Menu menu : menuList) {
			if (null != menu.getName() && !"".equals(menu.getName())) {
				info.addStringPermission(menu.getName());
			}
		}
	}
}	
