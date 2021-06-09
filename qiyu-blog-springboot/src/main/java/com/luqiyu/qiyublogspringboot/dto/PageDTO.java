package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一分页列表
 *
 * @author: 启誉
 * @create: 2021-05-30
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor // 返回无参构造器
public class PageDTO<T> {
    /**
     * 分页列表
     */
    private List<T> recordList;

    /**
     * 总数
     */
    private Long count;
}
