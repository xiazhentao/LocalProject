package com.example.localproject;

import com.example.localproject.interceptor.AppInfoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Autowired
	private AppInfoInterceptor appInfoInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(appInfoInterceptor).addPathPatterns("/**");


	}
}
