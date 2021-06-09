package com.luqiyu.qiyublogspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder //构建器，构建对象
@NoArgsConstructor // mybatis JavaType 与 JdbcType类型转换
@AllArgsConstructor // mybatis JavaType 与 JdbcType类型转换
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)//不需要
    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;

    /**
     * 是否为草稿 0否 1是
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


}
