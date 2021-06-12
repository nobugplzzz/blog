package com.luqiyu.qiyublogspringboot.handler;

import com.luqiyu.qiyublogspringboot.dto.UrlRoleDTO;
import com.luqiyu.qiyublogspringboot.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 该类的主要功能就是通过当前的请求地址，获取该地址需要的用户角色
 * <p>
 * .用户访问某资源/xxx时，FilterInvocationSecurityMetadataSource这个类的实现类
 * （本文是FilterInvocationSecurityMetadataSourceImpl）会调用getAttributes方法来进行资源匹配。
 * 它会读取数据库resource表中的所有记录，对/xxx进行匹配。若匹配成功，
 * 则将/xxx对应所需的角色组成一个 Collection<ConfigAttribute>返回；
 * 匹配不成功则说明/xxx不需要什么额外的访问权限；
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    /**
     * 接口角色列表
     */
    public static List<UrlRoleDTO> urlRoleList;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 加载接口角色（权限）列表
     */
    @PostConstruct
    private void loadDataSource() {
        urlRoleList = roleMapper.listUrlRoles();
    }


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 修改接口角色关系后重新加载
        if (CollectionUtils.isEmpty(urlRoleList)) {
            this.loadDataSource();
        }
        FilterInvocation fi = (FilterInvocation) object;
        // 获取用户请求方式
        String method = fi.getRequest().getMethod();
        // 获取用户请求Url
        String url = fi.getRequest().getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (UrlRoleDTO urlRoleDTO : urlRoleList) {
            if (antPathMatcher.match(urlRoleDTO.getUrl(), url) && urlRoleDTO.getRequestMethod().equals(method)) {
                List<String> roleList = urlRoleDTO.getRoleList();
                if (CollectionUtils.isEmpty(roleList)) {
                    return SecurityConfig.createList("disable");
                }
                // createList方法参数是 字符串数组
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
