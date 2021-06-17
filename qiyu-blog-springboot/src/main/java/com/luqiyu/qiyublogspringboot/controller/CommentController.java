package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.annotation.OptLog;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.CommentBackDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.service.CommentService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.DeleteVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}

