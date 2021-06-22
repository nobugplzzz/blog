package com.luqiyu.qiyublogspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * websocket消息？？？？传输对象，用于展示websocket发送的消息
 *
 * @author: 启誉
 * @create: 2021-06-20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebsocketMessageDTO {
    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据
     */
    private Object data;
}
