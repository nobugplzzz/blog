package com.luqiyu.qiyublogspringboot.constant;

/**
 * redis常量，作为文章的点赞量和浏览量的key存入redis
 *
 * @author: 启誉
 * @create: 2021-06-18
 **/
public class RedisPrefixConst {

    /**
     * 用户点赞文章,Hash类型的key，对应map<userInfoId,点赞文章集合>
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like";

    /**
     * 文章点赞量，Hash类型的key
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 文章浏览量，Hash类型的key
     */
    public static final String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 用户点赞评论，Hash类型的key
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like";

    /**
     * 评论点赞量，Hash类型的key
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 博客浏览量，String类型的key
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * ip集合，Set类型的key
     */
    public static final String IP_SET = "ip_set";


    /**
     * 关于我信息
     */
    public static final String ABOUT = "about";

    /**
     * 公告
     */
    public static final String NOTICE = "notice";
}
