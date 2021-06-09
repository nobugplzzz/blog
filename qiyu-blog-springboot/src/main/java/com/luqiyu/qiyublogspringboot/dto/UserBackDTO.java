package com.luqiyu.qiyublogspringboot.dto;

import lombok.*;

import java.util.Date;

/**
 * 后台用户列表传输对象
 *
 * @author: 启誉
 * @create: 2021-06-07
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBackDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户角色
     */
    private Integer roleId;

    /**
     * 是否禁用
     */
    private Integer isDisabled;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;
}
