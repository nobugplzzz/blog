package com.luqiyu.qiyublogspringboot.exception;

import com.alibaba.fastjson.JSON;
import com.luqiyu.qiyublogspringboot.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局接口参数校验异常处理类
 * 我们只需要在类上加上@ControllerAdvice注解这个类就成为了全局异常处理类
 * 使用@RestControllerAdvice + @ExceptionHandler(xxx.class)统一参数校验异常
 *
 * @author: 启誉
 * @create: 2021-06-12
 **/
@Slf4j
@RestControllerAdvice
public class GlobalControllerHandler {
    private static final String BAD_REQUEST_MSG = "客户端请求参数错误";
    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    public Result bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return new Result(false,HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
    }
    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return new Result(false,HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
    }
    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        return new Result(false,HttpStatus.BAD_REQUEST.value(), BAD_REQUEST_MSG, collect);
    }


}