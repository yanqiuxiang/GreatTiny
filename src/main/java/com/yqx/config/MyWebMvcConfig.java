package com.yqx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebMvcConfig extends WebMvcConfigurerAdapter{ 
	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		
		registry.addViewController("/").setViewName("/login");
		registry.addViewController("/main").setViewName("/main");
		registry.addViewController("/login").setViewName("/login");//登陆页面
		registry.addViewController("/welcome").setViewName("/welcome");//主页
		registry.addViewController("/onAuth").setViewName("/user/onAuth");//没有权限返回页面
		registry.addViewController("/user/login").setViewName("/login");//没有权限返回页面
		super.addViewControllers(registry);
	}
}
