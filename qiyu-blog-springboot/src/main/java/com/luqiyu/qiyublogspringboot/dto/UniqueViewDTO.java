package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网站访问量视图对象，用于后台首页展示访问量
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniqueViewDTO {
    /**
     * 日期
     */
    private String day;

    /**
     * 访问量
     */
    private Integer viewsCount;
}
