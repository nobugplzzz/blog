package com.luqiyu.qiyublogspringboot.controller;

import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.BlogHomeInfoDTO;
import com.luqiyu.qiyublogspringboot.service.BlogInfoService;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客信息模块
 *
 * @author: 启誉
 * @create: 2021-06-13
 **/
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {
    @Autowired
    private BlogInfoService blogInfoService;

    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<BlogHomeInfoDTO> getBlogBackInfo() {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", blogInfoService.getBlogBackInfo());
    }

    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    public Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", blogInfoService.getBlogInfo());
    }
}
