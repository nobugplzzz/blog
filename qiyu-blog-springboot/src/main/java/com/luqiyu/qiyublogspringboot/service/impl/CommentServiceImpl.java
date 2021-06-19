package com.luqiyu.qiyublogspringboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.constant.RedisPrefixConst;
import com.luqiyu.qiyublogspringboot.dto.*;
import com.luqiyu.qiyublogspringboot.entity.Comment;
import com.luqiyu.qiyublogspringboot.mapper.CommentMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.HTMLUtil;
import com.luqiyu.qiyublogspringboot.util.UserUtil;
import com.luqiyu.qiyublogspringboot.vo.CommentVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.DeleteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    public PageDTO<CommentDTO> listComments(Integer articleId, Long current) {
        // 查询文章评论量
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                .isNull(Objects.isNull(articleId), Comment::getArticleId)
                .isNull(Comment::getParentId)
                .eq(Comment::getIsDelete, CommonConst.FALSE));
        if (commentCount == 0) {
            return new PageDTO<>();
        }
        // 分页查询评论集合
        List<CommentDTO> commentDTOList = commentMapper.listComments(articleId, (current - 1) * 10);
        // 查询redis的评论点赞数据
        Map<String, Integer> likeCountMap = (Map<String, Integer>) redisTemplate.boundHashOps(RedisPrefixConst.COMMENT_LIKE_COUNT).entries();
        // 提取评论id集合
        List<Integer> commentIdList = new ArrayList<>();
        // 封装评论点赞量
        commentDTOList.forEach(item -> {
            commentIdList.add(item.getId());
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        // 根据评论id集合查询回复数据
        List<ReplyDTO> replyDTOList = commentMapper.listReplies(commentIdList);
        // 封装回复点赞量
        replyDTOList.forEach(item -> item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString())));
        // 根据评论id分组回复数据？？？？？？？？？？？方便封装进对应父评论，因为sql只是查出所有的评论和所有的回复评论，还需要处理
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream().collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 根据评论id查询回复量
        Map<Integer, Integer> replyCountMap = commentMapper.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId, ReplyCountDTO::getReplyCount));
        // 将分页回复数据和回复量封装进对应的评论
        commentDTOList.forEach(item -> {
            item.setReplyDTOList(replyMap.get(item.getId()));
            item.setReplyCount(replyCountMap.get(item.getId()));
        });
        return new PageDTO<>(commentDTOList, commentCount.longValue());
    }

    @Override
    public void saveComment(CommentVO commentVO) {
        // 过滤html标签
        commentVO.setCommentContent(HTMLUtil.deleteCommentTag(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getLoginUser().getUserInfoId())
                .replyId(commentVO.getReplyId())
                .articleId(commentVO.getArticleId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .createTime(new Date())
                .build();
        commentMapper.insert(comment);
        // 通知用户
        notice(commentVO);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentMapper.listRepliesByCommentId(commentId, (current - 1) * 5);
        // 查询redis的评论点赞数据
        Map<Integer,Integer> likeCountMap = redisTemplate.boundHashOps(RedisPrefixConst.COMMENT_LIKE_COUNT).entries();
        // 封装点赞数据
        replyDTOList.forEach(item->{
            item.setLikeCount(Objects.requireNonNull(likeCountMap).get(item.getId().toString()));
        });
        return replyDTOList;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveCommentLike(Integer commentId) {
        // 查询当前用户点赞过的评论id集合
        Object object = redisTemplate.boundHashOps(RedisPrefixConst.COMMENT_USER_LIKE).get(UserUtil.getLoginUser().getUserInfoId().toString());
        // 强转Set，不允许重复
        Set<Integer> commentLikeSet=(Set<Integer>)object;
        // 第一次点赞则创建
        if (CollectionUtils.isEmpty(commentLikeSet)){
            commentLikeSet=new HashSet<>();
        }
        // 判断是否已经点赞
        if (commentLikeSet.contains(commentId)){
            // 已经点赞则删除评论id
            commentLikeSet.remove(commentId);
            // 评论点赞量-1
            redisTemplate.boundHashOps( RedisPrefixConst.COMMENT_LIKE_COUNT).increment(commentId.toString(),-1);
        }else{
            // 评论点赞量+1
            redisTemplate.boundHashOps( RedisPrefixConst.COMMENT_LIKE_COUNT).increment(commentId.toString(),1);

        }
        // 保存点赞记录
        redisTemplate.boundHashOps(RedisPrefixConst.COMMENT_USER_LIKE).put(commentId.toString(),commentLikeSet);
    }

    /**
     * 通知评论用户
     *
     * @param commentVO 评论信息
     */
    @Async
    public void notice(CommentVO commentVO) {
        // 判断是回复用户还是评论作者
        Integer userId = Objects.nonNull(commentVO.getReplyId()) ? commentVO.getReplyId() : CommonConst.BLOGGER_ID;
        // 查询邮箱号
        String email = userInfoMapper.selectById(userId).getEmail();
        if (StringUtils.isNotBlank(email)) {
            // 判断页面路径
//            String url = Objects.nonNull(commentVO.getArticleId()) ? URL + ARTICLE_PATH + commentVO.getArticleId() : URL + LINK_PATH;
//            // 发送消息
//            EmailDTO emailDTO = EmailDTO.builder()
//                    .email(email)
//                    .subject("评论提醒")
//                    .content("您收到了一条新的回复，请前往" + url + "\n页面查看")
//                    .build();
//            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        }
    }


}