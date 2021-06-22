package com.luqiyu.qiyublogspringboot.dto;

import com.luqiyu.qiyublogspringboot.entity.ChatRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 聊天记录传输对象，用于展示聊天记录
 *
 * @author: 启誉
 * @create: 2021-06-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRecordDTO {
    /**
     * 聊天记录
     */
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;
}
