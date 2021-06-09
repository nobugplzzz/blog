package com.luqiyu.qiyublogspringboot.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.luqiyu.qiyublogspringboot.entity.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 添加/更新菜单视图对象
 *
 * @author: 启誉
 * @create: 2021-06-05
 **/
@Getter
@Setter
public class MenuVO {
    /**
     * 主键
     */
    @ApiModelProperty(name = "id", value = "主键",  dataType = "Integer")
    private Integer id;

    /**
     * 菜单名
     */
    @ApiModelProperty(name = "name", value = "菜单名",  dataType = "String")
    private String name;

    /**
     * 菜单路径
     */
    @ApiModelProperty(name = "url", value = "菜单路径",  dataType = "String")
    private String url;

    /**
     * 菜单icon
     */
    @ApiModelProperty(name = "icon", value = "菜单icon",  dataType = "String")
    private String icon;

    /**
     * 排序
     */
    @ApiModelProperty(name = "orderNum", value = "排序",  dataType = "Integer")
    private Integer orderNum;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型",  dataType = "Integer")
    private Integer type;


    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id",  dataType = "Integer")
    private Integer parentId;
}
