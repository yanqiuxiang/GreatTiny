package com.yqx.shiro;

import java.util.LinkedHashMap;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//My failure and greatness


/***
 * shiro配置类
 * */ 
@Configuration
public class ShiroConfig {
	
	private LinkedHashMap<String, String> getLinkedMap() {
		LinkedHashMap<String, String> filterMap = new LinkedHashMap<String,String>();
		filterMap.put("/user/loginUser", "anon");
		filterMap.put("/user/login", "anon");
		filterMap.put("/static/**", "anon");
		filterMap.put("/code", "anon");
		filterMap.put("/test", "anon");
		//授权过滤器
		//注意，当前授权拦截后,shiro会自动跳转到未授权页面
		filterMap.put("/**", "authc");
		return filterMap;
	}
	/**
	 * 创建ShiroFilterFactoryBean
	 * */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
	
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//1、设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
		//2、添加shiro内置过滤器
		/***
		 * 常见
		 * anon  无需认证
		 * authc 必须认证才可以访问
		 * user 必须使用rememberMe的功能可以直接访问
		 * perms 该资源必须资源权限才可以访问
		 * role 该资源必须得到角色权限才可以访问
		 * */
		shiroFilterFactoryBean.setFilterChainDefinitionMap(getLinkedMap());
		//修改调整的登录页面
		shiroFilterFactoryBean.setLoginUrl("/user/login");
		//修改调整的授权页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/user/onAuth");
		return shiroFilterFactoryBean;
	}
	
	/**
	 * 创建DefaultWebSecurityManager
	 * */
	@Bean
	public DefaultWebSecurityManager getDefaultWebSecurityManager() {
		
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//关联Realm
		securityManager.setRealm(getRealm());
		return securityManager;
	}
	/**
	 * 创建Realm
	 * */
	@Bean
	public MyRealm getRealm() {
		
		return new MyRealm();
	}
}
