package com.luqiyu.qiyublogspringboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.dto.ChatRecordDTO;
import com.luqiyu.qiyublogspringboot.dto.RecallMessageDTO;
import com.luqiyu.qiyublogspringboot.dto.WebsocketMessageDTO;
import com.luqiyu.qiyublogspringboot.entity.ChatRecord;
import com.luqiyu.qiyublogspringboot.enums.ChatTypeEnum;
import com.luqiyu.qiyublogspringboot.mapper.ChatRecordMapper;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.util.DateUtil;
import com.luqiyu.qiyublogspringboot.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket服务类
 *
 * @author: 启誉
 * @create: 2021-06-20
 **/
@Slf4j
@ServerEndpoint(value = "/websocket", configurator = WebSocketServiceImpl.ChatConfigurator.class)
@Service
public class WebSocketServiceImpl {
    /**
     * 用户session //与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 用户session集合 //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();

    private static ChatRecordMapper chatRecordMapper;

    @Autowired
    public void setChatRecordMapper(ChatRecordMapper chatRecordMapper) {
        WebSocketServiceImpl.chatRecordMapper = chatRecordMapper;
    }

    /**
     * 重写equals和hashCode方法
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WebSocketServiceImpl that = (WebSocketServiceImpl) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        // 加入连接
        this.session = session;
        webSocketSet.add(this);
        // 更新在线人数
        updateOnlineCount();
        // 加载历史聊天记录
        ChatRecordDTO chatRecordDTO = listChartRecords(endpointConfig);
        // 发送消息
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ChatTypeEnum.HISTORY_RECORD.getType())
                .data(chatRecordDTO)
                .build();
        // 线程安全的
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
        }

        WebSocketServiceImpl.sendInfo("这是服务端发来的消息","111");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebsocketMessageDTO messageDTO = JSON.parseObject(message, WebsocketMessageDTO.class);
        switch (Objects.requireNonNull(getChatType(messageDTO.getType()))) {
            case SEND_MESSAGE:
                // 发送消息
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), ChatRecord.class);
                chatRecordMapper.insert(chatRecord);
                messageDTO.setData(chatRecord);
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
                    }
                }
                break;
            case RECALL_MESSAGE:
                // 撤回消息
                RecallMessageDTO recallMessage = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), RecallMessageDTO.class);
                // 删除记录
                deleteRecord(recallMessage.getId());
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
                    }
                }
                break;
            case HEART_BEAT:
                // 心跳消息
                messageDTO.setData("pong");
                session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(messageDTO)));
            default:
                break;
        }

    }



    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws IOException {
        // 更新在线人数
        webSocketSet.remove(this);
        updateOnlineCount();
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    /**
     * 加载历史聊天记录
     *
     * @param endpointConfig 配置
     * @return 加载历史聊天记录
     */
    private ChatRecordDTO listChartRecords(EndpointConfig endpointConfig) {
        List<ChatRecord> chatRecordList = chatRecordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateTime, DateUtil.getBeforeHourTime(12)));
        String ipAddr = endpointConfig.getUserProperties().get(ChatConfigurator.HEADER_NAME).toString();
        return ChatRecordDTO.builder()
                .chatRecordList(chatRecordList)
                .ipAddr(ipAddr)
                .ipSource(IpUtil.getIpSource(ipAddr))
                .build();
    }

    /**
     * 获取客户端真实ip
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {

        public static String HEADER_NAME = "X-Real-IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                String firstFoundHeader = request.getHeaders().get(HEADER_NAME.toLowerCase()).get(0);
                sec.getUserProperties().put(HEADER_NAME, firstFoundHeader);
            } catch (Exception e) {
                sec.getUserProperties().put(HEADER_NAME, "未知ip");
            }
        }

    }

    /**
     * 更新在线人数
     *
     * @throws IOException io异常
     */
    private void updateOnlineCount() throws IOException {
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ChatTypeEnum.ONLINE_COUNT.getType())
                .data(webSocketSet.size())
                .build();
        for (WebSocketServiceImpl webSocketService : webSocketSet) {
            synchronized (webSocketService.session) {
                webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
            }
        }
    }

    /**
     * 删除记录
     *
     * @param id ID
     */
    @Async
    public void deleteRecord(Integer id) {
        chatRecordMapper.deleteById(id);
    }

    /**
     * 根据类型获取枚举
     * @param type 类型
     * @return 枚举
     */
    public static ChatTypeEnum getChatType(Integer type) {
        for (ChatTypeEnum chatType : ChatTypeEnum.values()) {
            if (chatType.getType().equals(type)) {
                return chatType;
            }
        }
        return null;
    }

    /**
     * 增加服务器主动推送消息功能
     */
    //实现服务器主动推送
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    //群发自定义消息
    public static void sendInfo(String message,@PathParam("sid") String sid)
            throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServiceImpl item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else{
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }
}
