package com.luqiyu.qiyublogspringboot.enums;

import org.springframework.http.HttpStatus;

/**
 * 异常错误码枚举类
 * 此枚举类中包含了异常的唯一标识、HTTP 状态码以及错误信息
 *
 * @author: 启誉
 * @create: 2021-06-12
 **/
public enum ErrorCodeEnum {
    /**
     * 未找到该资源
     */
    RESOURCE_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "未找到该资源"),
    /**
     * 请求数据格式验证失败
     */
    REQUEST_VALIDATION_FAILED(1002, HttpStatus.BAD_REQUEST, "请求数据格式验证失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 请求状态
     */
    private final HttpStatus status;

    /**
     * 异常信息
     */
    private final String message;

    ErrorCodeEnum(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "code=" + code +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}