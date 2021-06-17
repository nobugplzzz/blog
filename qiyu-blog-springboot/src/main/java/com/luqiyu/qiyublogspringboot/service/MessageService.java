package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.MessageBackDTO;
import com.luqiyu.qiyublogspringboot.dto.MessageDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MessageVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
public interface MessageService extends IService<Message> {

    /**
     * 查看后台留言列表
     *
     * @param condition 查询条件
     * @return 留言列表
     */
    PageDTO<MessageBackDTO> listMessageBackDTO(ConditionVO condition);

    /**
     * 查看留言弹幕
     *
     * @return 留言列表
     */
    List<MessageDTO> listMessages();

    /**
     * 添加留言弹幕
     *
     * @param messageVO 留言对象
     */
    void saveMessage(MessageVO messageVO);
}
