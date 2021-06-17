package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackDTO> listCommentBackDTO(@Param("condition") ConditionVO condition);

    /**
     * 统计后台评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Integer countCommentDTO(@Param("condition") ConditionVO condition);
}
