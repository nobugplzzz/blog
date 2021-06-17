package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章传输对象，用于分类或标签下的文章分类列表，因为有分类名或标签名前台需要展示，所以封装成对象
 *
 * @author: 启誉
 * @create: 2021-06-17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePreviewListDTO {
    /**
     * 条件对应的文章列表
     */
    private List<ArticlePreviewDTO> articlePreviewDTOList;

    /**
     * 条件名，分类名或标签名，前台需要展示
     */
    private String name;
}
