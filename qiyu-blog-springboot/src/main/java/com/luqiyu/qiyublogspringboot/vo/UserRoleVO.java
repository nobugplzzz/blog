package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 编辑用户提交的视图对象
 *
 * @author: 启誉
 * @create: 2021-06-07
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户权限")
public class UserRoleVO {

    /**
     * 用户id
     */
    @NotNull(message = "userInfoId不能为空")
    @ApiModelProperty(name = "userInfoId", value = "用户id", dataType = "Integer")
    private Integer userInfoId;

    /**
     * 用户角色
     */
    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty(name = "roleList", value = "角色id集合", dataType = "List<Integer>")
    private List<Integer> roleIdList;


    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "Integer")
    private String nickname;
}
