package com.luqiyu.qiyublogspringboot.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 后台文章的数据传输对象，比entity的Article多了点赞量和浏览量属性
 *
 * @author: 启誉
 * @create: 2021-06-02
 **/
@Getter // 对象 to json
@Setter
@Builder
public class ArticleBackDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;

    /**
     * 是否为草稿 0否 1是,自动生成是boolean类型，改为Integer
     */
    private Integer isDraft;

    /**
     * 是否逻辑删除  0否 1是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 文章分类名，需要多表查询
     */
    private String categoryName;

    /**
     * 文章标签，需要多表查询
     */
    private List<TagDTO> tagDTOList;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewsCount;
}
