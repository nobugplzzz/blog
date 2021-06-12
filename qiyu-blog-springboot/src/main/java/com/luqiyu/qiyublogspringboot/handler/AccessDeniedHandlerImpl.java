package com.luqiyu.qiyublogspringboot.handler;

import com.alibaba.fastjson.JSON;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.vo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SpringSecurity的访问url资源权限不足处理器
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(new Result(false, StatusCodeConst.ERROR,"没有操作权限！")));
    }
}
