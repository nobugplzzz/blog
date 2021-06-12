package com.luqiyu.qiyublogspringboot.handler;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ccessDecisionManager的实现类，用于查看当前用户的角色列表中是否具备需要的权限
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 当前用户所具有的权限（角色）列表，列表元素GrantedAuthority
        // 使用GrantedAuthority.getAuthority()获取权限，使用流获取集合元素的属性集合
        List<String> permissionList = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // 是否具备当前请求需要的权限
        for (ConfigAttribute item: configAttributes){
            if (permissionList.contains(item.getAttribute())){
                return;
            }
        }
        throw new AccessDeniedException("没有操作权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
