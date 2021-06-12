package com.luqiyu.qiyublogspringboot.handler;

import com.alibaba.fastjson.JSON;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SpringSecurity登录失败处理器
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new Result(false, StatusCodeConst.ERROR,exception.getMessage())));
    }
}
