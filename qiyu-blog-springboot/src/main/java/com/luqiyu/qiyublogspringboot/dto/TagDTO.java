package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 文章列表里文章标签的视图对象,id用于编辑，name用于显示名称
 *
 * @author: 启誉
 * @create: 2021-06-02
 **/
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 标签名
     */
    private String name;
}
