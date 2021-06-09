package com.luqiyu.qiyublogspringboot.vo;

import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import lombok.Getter;

import java.io.Serializable;

/**
 * 异步统一返回的结果封装，为什么是VO，不是DTO？
 * VO：视图对象，用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来。
 *
 * @author: 启誉
 * @create: 2021-05-29
 **/
@Getter // 为什么需要getter？jackson（springboot-web-stater自带）要使用反射getter获取key，返回值value作为json
public class Result<T> {
    /**
     * 是否成功
     * 状态码，200表示请求成功，400表示异常..
     * 结果消息
     * 结果数据
     */
    private boolean flag;
    private Integer code;
    private String message;
    private T data;

    /**
     * 有返回数据
     */
    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }

    /**
     * 无返回数据
     */
    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    /**
     * 无返回数据，成功
     */
    public Result() {
        this.flag = true;
        this.code = StatusCodeConst.OK;
        this.message = "操作成功!";
    }
}
