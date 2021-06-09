package com.luqiyu.qiyublogspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * 集成swagger
 * 要想使用swagger，得配置在springboot中，因为springboot没有这玩意
 *
 * @author: 启誉
 * @create: 2021-05-29
 **/
@Configuration // 配置类，springboot会扫描带这个注解的类
@EnableSwagger2// 开启Swagger2的自动配置
public class SwaggerConfig {

    @Bean //配置docket以配置Swagger具体参数
    public Docket docket(Environment environment) {
        // 设置要显示swagger的环境
        Profiles of = Profiles.of("dev", "test");
        // 判断当前是否处于该环境
        // 通过 enable() 接收此参数判断是否要显示
        boolean b = environment.acceptsProfiles(of);

        // Docket 实例关联上 apiInfo()
        return new Docket(DocumentationType.SWAGGER_2)
                        .apiInfo(apiInfo())
                        .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                        .apis(RequestHandlerSelectors.basePackage("com.luqiyu.qiyublogspringboot.controller"))
                        .paths(PathSelectors.any()) // 配置如何通过path过滤,即这里扫描任何请求
                        .build();
    }

    //配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("联系人名字", "http://xxx.xxx.com/联系人访问链接", "联系人邮箱");
        return new ApiInfo(
                "博客api文档", // 标题
                "springboot+vue开发的博客项目", // 描述
                "v1.0", // 版本
                "http://localhost:8080/组织链接", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "许可链接", // 许可连接
                new ArrayList<>()// 扩展
        );
    }

}
