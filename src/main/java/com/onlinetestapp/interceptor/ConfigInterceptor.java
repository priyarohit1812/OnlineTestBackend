package com.onlinetestapp.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigInterceptor implements WebMvcConfigurer {
	@Autowired
	UserRequestInterceptor userRequestInterceptor;
	@Autowired
	AdminRequestInterceptor adminRequestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.userRequestInterceptor).addPathPatterns("/user/**").excludePathPatterns("/user/login",
				"/user/register");

		registry.addInterceptor(this.adminRequestInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedHeaders("Authorization").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").exposedHeaders("Access-Control-Allow-Origin","Access-Control-Allow-Headers","Access-Control-Allow-Methods");
	}
}
