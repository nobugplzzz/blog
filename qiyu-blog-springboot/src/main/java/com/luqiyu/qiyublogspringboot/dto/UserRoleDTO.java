package com.luqiyu.qiyublogspringboot.dto;

import lombok.Data;

/**
 * 用户角色传输对象，用于前端显示用户角色；后端mapper查询RoleList的offType值
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
@Data
public class UserRoleDTO {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;
}
