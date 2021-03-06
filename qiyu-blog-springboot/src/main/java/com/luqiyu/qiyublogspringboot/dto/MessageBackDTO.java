package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 留言传输对象，用于后台展示留言列表
 *
 * @author: 启誉
 * @create: 2021-06-17
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageBackDTO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户ip地址
     */
    private String ipSource;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 留言时间
     */
    private Date createTime;
}
