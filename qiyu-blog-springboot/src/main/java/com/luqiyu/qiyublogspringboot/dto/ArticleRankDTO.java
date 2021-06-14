package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章排行视图对象，用于后台首页展示文章排行
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRankDTO {
    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewsCount;
}
