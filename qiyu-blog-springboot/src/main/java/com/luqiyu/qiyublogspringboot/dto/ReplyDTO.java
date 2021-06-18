package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 回复评论的传输对象，用于前台查询评论的回复评论，此回复评论也可回复，因此多了replyXX等被回复用户相关属性
 *
 * @author: 启誉
 * @create: 2021-06-17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    /**
     * 评论id
     */
    private Integer id;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 被回复用户id
     */
    private Integer replyId;

    /**
     * 被回复用户昵称
     */
    private String replyNickname;

    /**
     * 被回复个人网站
     */
    private String replyWebSite;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private Date createTime;
}
