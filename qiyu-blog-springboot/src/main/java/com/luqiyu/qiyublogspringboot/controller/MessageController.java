package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.annotation.OptLog;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.MessageBackDTO;
import com.luqiyu.qiyublogspringboot.dto.MessageDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.service.MessageService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MessageVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
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
public class MessageController {

    @Autowired
    MessageService messageService;

    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    public Result saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return new Result<>(true, StatusCodeConst.OK, "添加成功");
    }

    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    public Result<List<MessageDTO>> listMessages() {
        return new Result<>(true, StatusCodeConst.OK, "添加成功", messageService.listMessages());
    }

    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/messages")
    public Result<PageDTO<MessageBackDTO>> listMessageBackDTO(ConditionVO condition) {
        return new Result<>(true, StatusCodeConst.OK, "添加成功", messageService.listMessageBackDTO(condition));
    }

    @OptLog(optType = CommonConst.REMOVE)
    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/messages")
    public Result deleteMessages(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }
}

