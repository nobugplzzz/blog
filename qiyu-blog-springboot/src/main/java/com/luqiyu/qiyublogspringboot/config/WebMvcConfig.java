package com.luqiyu.qiyublogspringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMVC的web配置，关于web的自定义配置
 *
 * @author: 启誉
 * @create: 2021-05-30
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域处理
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowCredentials(true) // 是否发送cookie
                .allowedHeaders("*") // 允许头部设置
                .allowedOriginPatterns("*") // 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE"); // 允许请求方法
//                .allowedMethods("*");    // 允许请求方法
    }

}
