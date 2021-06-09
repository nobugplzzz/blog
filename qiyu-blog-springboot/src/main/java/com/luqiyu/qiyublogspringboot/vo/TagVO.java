package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 标签新增或更新的视图对象
 *
 * @author: 启誉
 * @create: 2021-06-01
 **/
@Setter
@Getter // get属性 对象 to json
public class TagVO {


    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "标签id", dataType = "Integer")
    private Long id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @ApiModelProperty(name = "categoryName", value = "标签名", required = true, dataType = "String")
    private String name;

}
