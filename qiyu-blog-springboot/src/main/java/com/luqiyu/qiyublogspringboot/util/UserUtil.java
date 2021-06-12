package com.luqiyu.qiyublogspringboot.util;

import com.luqiyu.qiyublogspringboot.dto.UserInfoDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户工具类
 * 用于封装用户登录信息、获取当前登录用户
 *
 * @author: 启誉
 * @create: 2021-06-10
 **/
public class UserUtil {

    /**
     * 获取当前登录用户（实现UserDetails的实例）,也就是convertLoginUser封装好的UserInfoDTO实例
     *
     * Spring Security使用一个Authentication对象来描述当前用户的相关信息。SecurityContextHolder中持有
     * 的是当前用户的SecurityContext，而SecurityContext持有的是代表当前用户相关信息的Authentication的
     * 引用。这个Authentication对象不需要我们自己去创建，在与系统交互的过程中，Spring Security会自动为我
     * 们创建相应的Authentication对象，然后赋值给当前的SecurityContext。但是往往我们需要在程序中获取当前
     * 用户的相关信息，比如最常见的是获取当前登录用户的用户名。在程序的任何地方，通过如下方式我们可以获取到当前
     * 用户的用户名。
     * 通过Authentication.getPrincipal()可以获取到代表当前用户的信息，这个对象通常是UserDetails的实例。
     * 获取当前用户的用户名是一种比较常见的需求，关于上述代码其实Spring Security在Authentication中的实现
     * 类中已经为我们做了相关实现，所以获取当前用户的用户名最简单的方式应当如下。
     *
     * @return 登录用户信息
     */
    public static UserInfoDTO getLoginUser() {
        return (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 封装用户登录信息
     *
     * @param userAuth 用户账号
     * @param userInfo 用户信息
     * @param roleList 用户角色列表
     * @param request  请求
     * @return 用户登录信息
     */
    public static UserInfoDTO convertLoginUser(UserAuth userAuth, UserInfo userInfo, List<String> roleList, HttpServletRequest request) {
        // 获取特殊登录信息，ip相关和User-Agent相关
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 封装用户登录信息
        return UserInfoDTO.builder()
                .id(userAuth.getId())
                .loginType(userAuth.getLoginType())
                .userInfoId(userInfo.getId())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .email(userInfo.getEmail())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .ipAddr(ipAddr)
                .ipSource(ipSource)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(new Date())
                .build();
    }
}
