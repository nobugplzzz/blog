package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserBackDTO;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.service.UserAuthService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.PasswordVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import com.luqiyu.qiyublogspringboot.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Slf4j
@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {

    @Autowired
    UserAuthService userAuthService;

    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageDTO<UserBackDTO>> listUsers(ConditionVO conditionVO){
        log.error("conditionVO:"+conditionVO);
        return new Result<>(true, StatusCodeConst.OK,"查询成功",userAuthService.listUsersBackDTO(conditionVO));
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/users")
    public Result saveUser(@Validated @RequestBody UserVO user){
        log.error("bindingResult:");
        userAuthService.saveUser(user);
        return new Result<>(true, StatusCodeConst.OK,"注册成功");
    }
}

