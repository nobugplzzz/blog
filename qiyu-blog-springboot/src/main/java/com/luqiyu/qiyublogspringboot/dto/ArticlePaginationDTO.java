package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章传输对象，用于文章页面上一篇/下一篇文章展示
 *
 * @author: 启誉
 * @create: 2021-06-16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePaginationDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

}
