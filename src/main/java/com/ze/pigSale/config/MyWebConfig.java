package com.ze.pigSale.config;


import com.ze.pigSale.common.JacksonObjectMapper;
import com.ze.pigSale.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * author: zebii
 * Date: 2023-01-28-20:35
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        LoginInterceptor loginInterceptor = new LoginInterceptor();
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/user/login",
//                        "/user/logout",
//                        "/common/**",
//                        "/index.html",
//                        "/js/**",
//                        "/css/**",
//                        "/img/**",
//                        "/fonts/**",
//                        "//favicon.ico",
//                        "/user/sendMsg",
//                        "/");
//    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, messageConverter);
    }
}
