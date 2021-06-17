package com.luqiyu.qiyublogspringboot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 统一查询条件
 * 或者说封装一个查询条件视图对象，某个视图或组件的数据封装起来
 * 条件有分页和查询字段
 *
 * @author: 启誉
 * @create: 2021-05-31
 **/
@Getter // 对象 to json
@Setter //json to 对象
@ToString // 打印测试
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("查询条件") //Swagger注解，pojo注释
public class ConditionVO {

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "current", value = "当前页码", required = true, dataType = "Integer")
    private Integer current;

    /**
     * 显示数量
     */
    @ApiModelProperty(name = "size", value = "显示数量", required = true, dataType = "Integer")
    private Integer size;

    /**
     * 搜索内容
     * 去掉 required = true，即非必须的
     */
    @ApiModelProperty(name = "keywords", value = "搜索内容", dataType = "String")
    private String keywords;

    /**
     * 是否逻辑删除
     * 文章表字段,查询用,筛选逻辑删除的
     */
    @ApiModelProperty(name = "isDeleted", value = "是否逻辑删除", dataType = "Integer")
    private Integer isDeleted;

    /**
     * 是否为草稿
     * 文章表字段,查询用，筛选是草稿的
     */
    @ApiModelProperty(name = "isDraft", value = "草稿状态", dataType = "Integer")
    private Integer isDraft;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "Date")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "Date")
    private Date endTime;


    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签id
     */
    @ApiModelProperty(name = "tagId", value = "标签id", dataType = "Integer")
    private Integer tagId;
}
