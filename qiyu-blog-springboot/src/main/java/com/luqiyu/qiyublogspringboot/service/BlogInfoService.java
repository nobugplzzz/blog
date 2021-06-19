package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.BlogBackInfoDTO;
import com.luqiyu.qiyublogspringboot.dto.BlogHomeInfoDTO;

/**
 * 博客前后台信息展示服务接口
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
public interface BlogInfoService {

    /**
     * 获取后台首页数据
     *
     * @return 博客后台信息
     */
    BlogBackInfoDTO getBlogBackInfo();

    /**
     * 获取前台首页博客数据
     *
     * @return 前台首页博客对象
     */
    BlogHomeInfoDTO getBlogInfo();

    /**
     * 获取关于我内容
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 修改关于我内容
     * @param aboutContent 关于我内容
     */
    void updateAbout(String aboutContent);

    /**
     * 修改公告
     * @param notice 公告
     */
    void updateNotice(String notice);

    /**
     * 后台查看公告
     * @return 公告
     */
    String getNotice();
}
