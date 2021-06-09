package com.luqiyu.qiyublogspringboot.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author: 启誉
 * @create: 2021-06-08
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * @return 操作类型
     */
    String optType() default "";

}

