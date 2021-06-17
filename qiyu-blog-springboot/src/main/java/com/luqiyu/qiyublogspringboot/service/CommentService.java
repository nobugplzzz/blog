package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.DeleteVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
public interface CommentService extends IService<Comment> {


    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论列表
     */
    PageDTO<CommentBackDTO> listCommentBackDTO(ConditionVO condition);

    /**
     * 恢复或删除评论
     *
     * @param deleteVO 逻辑删除对象
     */
    void updateCommentDelete(DeleteVO deleteVO);
}
