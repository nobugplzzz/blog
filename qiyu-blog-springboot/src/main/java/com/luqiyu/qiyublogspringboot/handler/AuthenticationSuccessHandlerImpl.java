package com.luqiyu.qiyublogspringboot.handler;

import com.alibaba.fastjson.JSON;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.UserInfoDTO;
import com.luqiyu.qiyublogspringboot.dto.UserLoginDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.util.UserUtil;
import com.luqiyu.qiyublogspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * SpringSecurity登录成功处理器
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    UserAuthMapper userAuthMapper;

    /**
     * 登录成功就返回JSON数据（Result<UserInfoDTO>）给前端
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 更新用户ip，最近登录时间
        updateUserInfo();
        // 把登录用户的部分信息复制给UserLoginDTO，返回
        UserLoginDTO userLoginDTO = BeanCopyUtil.copyObject(UserUtil.getLoginUser(), UserLoginDTO.class);
        // 直接返回JSON需要这个
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(new Result<UserInfoDTO>(true, StatusCodeConst.OK, "登录成功！", userLoginDTO)));
    }

    /**
     * 更新部分用户信息，ip相关和lastLoginTime，都是UserAuth实体属性
     */
    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtil.getLoginUser().getId())
                .ipAddr(UserUtil.getLoginUser().getIpAddr())
                .ipSource(UserUtil.getLoginUser().getIpSource())
                .lastLoginTime(UserUtil.getLoginUser().getLastLoginTime())
                .build();
        userAuthMapper.updateById(userAuth);
    }
}
