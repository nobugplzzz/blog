package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编辑用户提交的视图对象
 *
 * @author: 启誉
 * @create: 2021-06-07
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {

    /**
     * 用户id
     */
    @ApiModelProperty(name = "id", value = "用户id", dataType = "Integer")
    private Integer id;

    /**
     * 角色id
     */
    @ApiModelProperty(name = "roleId", value = "角色id", dataType = "Integer")
    private Integer roleId;

    /**
     * 是否禁用
     */
    @ApiModelProperty(name = "isDisabled", value = "是否禁用", dataType = "Integer")
    private Integer isDisabled;

    /**
     * 昵称
     */
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "Integer")
    private String nickname;
}
