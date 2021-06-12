package com.luqiyu.qiyublogspringboot.dto;

import java.util.Date;
import java.util.List;

/**
 * 登录用户的传输对象，是完整用户信息UserInfoDTO的部分
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
public class UserLoginDTO {
    /**
     * 用户账号id
     */
    private Integer id;

//    /**
//     * 用户角色,集合元素使用的是String类型，而不是Role对象，需要看一下创建这个对象的方法赋值是什么
//     */
//    private List<String> roleList;

//    /**
//     * 浏览器
//     */
//    private String browser;
//
//    /**
//     * 操作系统
//     */
//    private String os;

    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 登录方式
     */
    private Integer loginType;


    /**
     * 用户名
     */
    private String username;

//    /**
//     * 密码
//     */
//    private String password;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

//    /**
//     * 是否禁用
//     */
//    private Integer isDisabled;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 个人网站
     */
    private String webSite;
}
