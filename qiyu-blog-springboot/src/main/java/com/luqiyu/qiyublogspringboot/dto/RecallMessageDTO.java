package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 撤回消息传输对象，用于聊天室撤回消息
 *
 * @author: yezhiqiu
 * @date: 2021-03-30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecallMessageDTO {

    /**
     * 消息id
     */
    private Integer id;

    /**
     * 是否为语音
     */
    private Boolean isVoice;

}
