package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.dto.MessageBackDTO;
import com.luqiyu.qiyublogspringboot.dto.MessageDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Message;
import com.luqiyu.qiyublogspringboot.mapper.MessageMapper;
import com.luqiyu.qiyublogspringboot.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.util.IpUtil;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    MessageMapper messageMapper;
    @Resource
    private HttpServletRequest request;

    @Override
    public PageDTO<MessageBackDTO> listMessageBackDTO(ConditionVO condition) {
        // 分页查询留言列表
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        LambdaQueryWrapper<Message> messageLambdaQueryWrapper = new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getIpAddress, Message::getIpSource, Message::getMessageContent, Message::getCreateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Message::getNickname, condition.getKeywords())
                .orderByDesc(Message::getCreateTime);
        Page<Message> messagePage = messageMapper.selectPage(page, messageLambdaQueryWrapper);
        // 转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanCopyUtil.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageDTO<>(messageBackDTOList, messagePage.getTotal());
    }

    @Override
    public List<MessageDTO> listMessages() {
        // 查询留言列表
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime));
        return BeanCopyUtil.copyList(messageList, MessageDTO.class);
    }

    @Override
    public void saveMessage(MessageVO messageVO) {
        // 获取用户ip
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        Message message = Message.builder()
                .nickname(messageVO.getNickname())
                .avatar(messageVO.getAvatar())
                .messageContent(messageVO.getMessageContent())
                .time(messageVO.getTime())
                .createTime(new Date())
                .ipAddress(IpUtil.getIpAddr(request))
                .ipSource(ipSource).build();
        messageMapper.insert(message);
    }
}
