package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * 用户注册视图对象
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户注册")
public class UserVO {
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名", value = "username", required = true, dataType = "String")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(name = "密码", value = "password", required = true, dataType = "String")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码不能少于6位")
    private String password;
}
