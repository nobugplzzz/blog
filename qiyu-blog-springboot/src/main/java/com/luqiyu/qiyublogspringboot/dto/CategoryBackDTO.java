package com.luqiyu.qiyublogspringboot.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 后台分类列表
 *
 * @author: 启誉
 * @create: 2021-05-30
 **/
@Getter // 用途：对象 to json
@Setter // 用途：Bean复制属性
public class CategoryBackDTO {

    /**
     * id
     */
    private Integer id;
    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类下的文章数量
     */
    private Integer articleCount;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
