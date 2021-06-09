package com.luqiyu.qiyublogspringboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文章选项传输对象，传输两个集合，分类和标签
 *
 * @author: 启誉
 * @create: 2021-06-03
 **/
@Getter
@Builder
public class ArticleOptionDTO {

    /**
     * 文章标签选项
     */
    List<TagDTO> tagDTOList;

    /**
     * 文章分类选项
     */
    List<CategoryBackDTO> categoryBackDTOList;
}
