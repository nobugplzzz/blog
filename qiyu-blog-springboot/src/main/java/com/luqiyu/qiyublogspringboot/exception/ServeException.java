package com.luqiyu.qiyublogspringboot.exception;

/**
 * 自定义异常类
 * 一般我们处理的都是 RuntimeException ，所以如果你需要自定义异常类型的话直接集成这个类就可以了。
 * @author 11921
 */
public class ServeException extends RuntimeException {
    private String message;

    public ServeException(String message) {
        super(message);
        this.message = message;
    }

}