/**
 * Copyright (c) 2006-2015 Kingdee Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Kingdee. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Kingdee,http://www.Kingdee.com.
 *  
 */
package com.kingdee.uranus.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * MVC Configuration
 * </p>
 * 
 * @author Administrator
 * @date 2018年3月24日 下午2:58:35
 * @version
 */
@Configuration
public class CustomMVCConfiguration implements WebMvcConfigurer {

	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("GBK"));
		return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(responseBodyConverter());
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}
}