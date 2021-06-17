package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.dto.CommentDTO;
import com.luqiyu.qiyublogspringboot.dto.ReplyCountDTO;
import com.luqiyu.qiyublogspringboot.dto.ReplyDTO;
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

    /**
     * 查看评论
     *
     * @param articleId 文章id
     * @param current   当前页码
     * @return 评论集合
     */
    List<CommentDTO> listComments(@Param("articleId") Integer articleId, @Param("current") Long current);

    /**
     * 查看评论id集合下的回复
     *
     * @param commentIdList 评论id集合
     * @return 回复集合
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id集合
     * @return 回复数量
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查看当条评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @return 回复集合
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("commentId") Integer commentId, @Param("current") Long current);
}
