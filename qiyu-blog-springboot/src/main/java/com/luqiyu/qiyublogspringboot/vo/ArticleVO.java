package com.luqiyu.qiyublogspringboot.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 新建或更新文章的视图对象，不需要isDeleted,createTime,updateTime属性，多了tagDTOList
 *
 * @author: 启誉
 * @create: 2021-06-02
 **/
@Getter
@Setter
@Builder
@ApiModel(description = "后台文章VO")
public class ArticleVO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Long id;


    /**
     * 文章分类
     */
    @ApiModelProperty(name = "categoryId", value = "文章分类", dataType = "Integer")
    private Integer categoryId;

    /**
     * 文章标签
     */
    @ApiModelProperty(name = "tagIdList", value = "文章标签", dataType = "List<Integer>")
    private List<Integer> tagIdList;


    /**
     * 文章缩略图
     */
    @ApiModelProperty(name = "articleCover", value = "文章缩略图", dataType = "String")
    private String articleCover;

    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    /**
     * 内容
     */
    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;


    /**
     * 是否置顶 0否 1是
     */
    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    /**
     * 是否为草稿 0否 1是 是否需要？？？？？？？？？？
     */
    @ApiModelProperty(name = "isDraft", value = "是否为草稿", dataType = "Integer")
    private Integer isDraft;


}
