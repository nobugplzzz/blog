package com.luqiyu.qiyublogspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat_record")
public class ChatRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;


}
