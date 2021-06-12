package com.luqiyu.qiyublogspringboot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举类
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(0, "邮箱登录"),
    /**
     * QQ登录
     */
    QQ(1, "QQ登录"),
    /**
     * 微博登录
     */
    WEIBO(2, "微博登录");


    /**
     * 登录方式
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;

    }
