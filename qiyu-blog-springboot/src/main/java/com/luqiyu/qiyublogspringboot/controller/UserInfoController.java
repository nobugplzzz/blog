package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.annotation.OptLog;
import com.luqiyu.qiyublogspringboot.constant.OptTypeConst;
import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.service.UserInfoService;
import com.luqiyu.qiyublogspringboot.vo.Result;
import com.luqiyu.qiyublogspringboot.vo.UserInfoVO;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@Api(tags="用户信息模块")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable/{userInfoId}")
    public Result updateUserSilence(@PathVariable("userInfoId") Integer userInfoId, Integer isDisabled) {
        log.error("userInfoId:"+userInfoId);
        log.error("isDisabled:"+isDisabled);
        userInfoService.updateUserDisable(userInfoId, isDisabled);
        return new Result<>(true, StatusCodeConst.OK, "修改成功！");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    public Result<String> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return new Result<>(true, StatusCodeConst.OK, "修改成功！");
    }

    @ApiOperation(value = "修改用户资料")
    @PutMapping("/users/info")
    public Result updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return new Result<>(true, StatusCodeConst.OK, "修改成功！");
    }


}

