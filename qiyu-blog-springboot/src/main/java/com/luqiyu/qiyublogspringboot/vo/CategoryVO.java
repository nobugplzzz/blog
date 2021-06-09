package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 新建分类或编辑分类提交的视图对象
 *
 * @author: 启誉
 * @create: 2021-05-31
 **/
@Setter //json to 对象
@Getter // get属性 对象 to json
public class CategoryVO {

    /**
     * id ,新建或更新对象使用
     */
    @ApiModelProperty(name = "id", value = "分类id", dataType = "Integer")
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    @ApiModelProperty(name = "categoryName", value = "分类名", required = true, dataType = "String")
    private String name;


}
