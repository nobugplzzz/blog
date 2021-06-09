package com.luqiyu.qiyublogspringboot.constant;

/**
 * 返回状态码常量
 *
 * @author: 启誉
 * @create: 2021-05-29
 **/
public class StatusCodeConst {

    /**
     * 请求成功
     */
    public static final int OK=20000;

    /**
     * 请求失败
     */
    public static final int ERROR = 20001;

    /**
     * 系统异常
     */
    public static final int SYSTEM_ERROR = 50000;

    /**
     * 未登录
     */
    public static final int NOT_LOGIN = 40001;

    /**
     * 没有操作权限
     */
    public static final int AUTHORIZED = 40003;
}
