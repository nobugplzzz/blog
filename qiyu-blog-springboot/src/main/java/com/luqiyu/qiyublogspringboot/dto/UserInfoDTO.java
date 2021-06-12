package com.luqiyu.qiyublogspringboot.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luqiyu.qiyublogspringboot.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户登录时加载的用户信息，实现UserDetails接口扩展信息，后序实现UserDetailService接口返回此对象信息
 * 用户对象信息包括：user_auth实体的属性和user_info实体的属性，加上用户角色列表属性,id使用userAuth的id
 *
 * @author: 启誉
 * @create: 2021-06-10
 **/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO implements UserDetails {

    /**
     * 用户账号id
     */
    private Integer id;

    /**
     * 用户角色,集合元素使用的是String类型，而不是Role对象，需要看一下创建这个对象的方法赋值是什么
     */
    private List<String> roleList;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 登录方式
     */
    private Integer loginType;


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 是否禁用
     */
    private Integer isDisabled;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * UserDetails 接口默认有几个方法需要实现，这几个方法中，除了 username和password 返回了
     * 此对象的username和password 之外，其他的方法我都统一返回 true，因为我这里的业务逻辑并不涉及到账户的锁定、
     * 密码的过期等等，只有账户是否被禁用，因此只处理了 isEnabled 方法，这一块小伙伴可以根据自己的实际情
     * 况来调整。
     */

    /**
     * 获取当前用户所具有的角色
     * （GrantedAuthority：授权）
     *
     * @return GrantedAuthority接口的集合，有getAuthority()方法获取角色名称
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 直接从 roles 中获取当前用户所具有的角色，构造 SimpleGrantedAuthority 然后返回即可。
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
