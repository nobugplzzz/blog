package com.luqiyu.qiyublogspringboot.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 新建或更新的角色视图对象
 *
 * @author: 启誉
 * @create: 2021-06-06
 **/
@Getter
@Setter
public class RoleVO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleLabel;


    /**
     * 角色备注
     */
    private String roleRemark;
}
