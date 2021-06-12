package com.luqiyu.qiyublogspringboot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举类
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN(1,"管理员","admin"),
    /**
     * 用户
     */
    USER(2,"用户","user"),
    /**
     * 测试
     */
    TEST(3,"测试","test");

    /**
     * 角色id
     */
    private final int roleId;
    /**
     * 角色名称
     */
    private final String roleName;
    /**
     * 角色（权限）标签
     */
    private final String roleLabel;
}
