package com.luqiyu.qiyublogspringboot.dto;

import lombok.Data;

import java.util.List;

/**
 * 访问接口所需角色传输对象，用于查询访问接口所需角色
 *
 * @author: 启誉
 * @create: 2021-06-11
 **/
@Data
public class UrlRoleDTO {
    /**
     * 资源id
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;
    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 角色名
     */
    private List<String> roleList;

    /**
     * 是否匿名
     */
    private Integer isAnonymous;
}
