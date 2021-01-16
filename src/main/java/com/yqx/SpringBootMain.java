package com.yqx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
@MapperScan(basePackages = {"com.yqx.dao"})
@SpringBootApplication(scanBasePackages = { "com.yqx.dao" })
public class SpringBootMain extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		 
        return application.sources(SpringBootMain.class);
    }

    public static void main(String[] args) throws Exception {
    	
        SpringApplication.run(SpringBootMain.class, args);
    }	
    
    @Bean
	public CorsFilter corsFilter(){ //设置跨域
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**",corsConfiguration);
        return  new CorsFilter(source);
    }
}