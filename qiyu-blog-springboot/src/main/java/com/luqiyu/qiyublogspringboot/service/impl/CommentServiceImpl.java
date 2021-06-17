package com.luqiyu.qiyublogspringboot.service.impl;

import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Comment;
import com.luqiyu.qiyublogspringboot.mapper.CommentMapper;
import com.luqiyu.qiyublogspringboot.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.DeleteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public PageDTO<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 统计后台评论量
        Long count = commentMapper.countCommentDTO(condition).longValue();
        if (count == 0) {
            return new PageDTO<>();
        }
        // 查询后台评论集合
        List<CommentBackDTO> commentBackDTOList = commentMapper.listCommentBackDTO(condition);
        // 获取评论点赞量
//        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps(COMMENT_LIKE_COUNT).entries();
        //封装点赞量
//        commentBackDTOList.forEach(item -> item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString())));
        return new PageDTO<>(commentBackDTOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCommentDelete(DeleteVO deleteVO) {
        // 修改评论逻辑删除状态
        List<Comment> commentList = deleteVO.getIdList().stream()
                .map(id -> Comment.builder().id(id).isDelete(deleteVO.getIsDelete()).build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }
}
