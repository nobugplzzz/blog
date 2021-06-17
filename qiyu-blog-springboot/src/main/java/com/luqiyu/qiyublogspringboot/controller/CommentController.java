package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.annotation.OptLog;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.dto.CommentDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.ReplyDTO;
import com.luqiyu.qiyublogspringboot.service.CommentService;
import com.luqiyu.qiyublogspringboot.vo.CommentVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.DeleteVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-17
 */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;


    @OptLog(optType = CommonConst.UPDATE)
    @ApiOperation(value = "删除或恢复评论")
    @PutMapping("/admin/comments")
    public Result deleteComment(DeleteVO deleteVO) {
        commentService.updateCommentDelete(deleteVO);
        return new Result<>(true, StatusCodeConst.OK, "操作成功！");
    }

    @OptLog(optType = CommonConst.REMOVE)
    @ApiOperation(value = "物理删除评论")
    @DeleteMapping("/admin/comments")
    public Result deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return new Result<>(true, StatusCodeConst.OK, "操作成功！");
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public Result<PageDTO<CommentBackDTO>> listCommentBackDTO(ConditionVO condition) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", commentService.listCommentBackDTO(condition));
    }

    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long")
    })
    @GetMapping("/comments")
    public Result<PageDTO<CommentDTO>> listComments(Integer articleId, Long current) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功！", commentService.listComments(articleId, current));
    }

    @ApiOperation(value = "添加评论或回复")
    @PostMapping("/comments")
    public Result saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return new Result<>(true, StatusCodeConst.OK, "评论成功！");
    }

    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "文章id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long")
    })
    @GetMapping("/comments/replies/{commentId}")
    public Result<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId, Long current) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功！", commentService.listRepliesByCommentId(commentId, current));
    }

    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/like")
    public Result saveCommentList(Integer commentId) {
        commentService.saveCommentLike(commentId);
        return new Result<>(true, StatusCodeConst.OK, "点赞成功！");
    }
}

