package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.luqiyu.qiyublogspringboot.dto.UserInfoDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.luqiyu.qiyublogspringboot.entity.UserRole;
import com.luqiyu.qiyublogspringboot.exception.ServeException;
import com.luqiyu.qiyublogspringboot.mapper.RoleMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
//import com.luqiyu.qiyublogspringboot.mapper.UserRoleMapper;
import com.luqiyu.qiyublogspringboot.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * UserDetailsService的实现类，用来执行登录等操作,主要是实现了 UserDetailsService 接口中
 * 的 loadUserByUsername 方法,在执行登录的过程中，这个方法将根据用户名去查找用户，如果用户不存在，
 * 则抛出 UsernameNotFoundException 异常，否则直接将查到的账号、账号信息、角色，并封装成 UserDetails的实现类 返回。
 *
 * @author: 启誉
 * @create: 2021-06-10
 **/
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    RoleMapper roleMapper;
    @Resource
    HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username){
        if (StringUtils.isBlank(username)) {
            throw new ServeException("用户名不能为空！");
        }
        // 查找账号是否存在
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                // select id name ..
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLoginType)
                // where id=...
                .eq(UserAuth::getUsername, username));
        if (Objects.isNull(userAuth)) {
            throw new ServeException("用户不存在!");
        }

        // 查询账号信息
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getId, UserInfo::getEmail, UserInfo::getNickname, UserInfo::getAvatar, UserInfo::getIntro, UserInfo::getWebSite, UserInfo::getIsDisabled)
                .eq(UserInfo::getId, userAuth.getUserInfoId()));

        // 查询账号角色（role_label),双表查询，自己写sql
        // 说明我之前的想法"UserAuth是用户"是错的，实现UserDetail接口的才是用户，UserAuth是账号，
        // UserInfo是账号信息
        List<String> roleList = roleMapper.listRoleByUserInfoId(userInfo.getId());

        // 查询账号点赞
        // 先不写。。。

        // 封装登录信息
        UserInfoDTO userInfoDTO = UserUtil.convertLoginUser(userAuth, userInfo, roleList, request);
        return userInfoDTO;
    }
}
