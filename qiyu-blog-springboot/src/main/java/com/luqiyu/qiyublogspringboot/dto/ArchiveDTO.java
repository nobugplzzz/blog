package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 文章归档视图对象，用于前台查看文章归档
 *
 * @author: 启誉
 * @create: 2021-06-14
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private Date createTime;
}
