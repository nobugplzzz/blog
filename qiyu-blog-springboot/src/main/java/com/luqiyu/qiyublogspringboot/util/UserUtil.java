//package com.luqiyu.qiyublogspringboot.util;
//
//import com.luqiyu.qiyublogspringboot.dto.UserInfoDTO;
//import com.luqiyu.qiyublogspringboot.entity.UserAuth;
//import com.luqiyu.qiyublogspringboot.entity.UserInfo;
//import eu.bitwalker.useragentutils.UserAgent;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
///**
// * 用户工具类
// * 1.获取当前登录用户
// * 2.封装用户登录信息
// *
// * @author: 启誉
// * @create: 2021-06-08
// **/
//public class UserUtil {
//    /**
//     * 获取当前登录用户
//     *
//     * @return 用户登录信息
//     */
//    public static UserInfoDTO getLoginUser() {
//        return (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
//
//    /**
//     * 封装用户登录信息
//     *
//     * @param user           用户账号
//     * @param userInfo       用户信息
//     * @param articleLikeSet 点赞文章id集合
//     * @param commentLikeSet 点赞评论id集合
//     * @param request        请求
//     * @return 用户登录信息
//     */
//    public static UserInfoDTO convertLoginUser(UserAuth user, UserInfo userInfo, List<String> roleList, Set<Integer> articleLikeSet, Set<Integer> commentLikeSet, HttpServletRequest request) {
//        // 获取登录信息
//        String ipAddr = IpUtil.getIpAddr(request);
//        String ipSource = IpUtil.getIpSource(ipAddr);
//        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
//        // 封装权限集合
//        return UserInfoDTO.builder()
//                .id(user.getId())
//                .userInfoId(userInfo.getId())
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .email(userInfo.getEmail())
//                .roleList(roleList)
//                .nickname(userInfo.getNickname())
//                .avatar(userInfo.getAvatar())
//                .intro(userInfo.getIntro())
//                .webSite(userInfo.getWebSite())
//                .articleLikeSet(articleLikeSet)
//                .commentLikeSet(commentLikeSet)
//                .ipAddr(ipAddr)
//                .ipSource(ipSource)
//                .browser(userAgent.getBrowser().getName())
//                .os(userAgent.getOperatingSystem().getName())
//                .lastLoginTime(new Date())
//                .build();
//    }
//}
