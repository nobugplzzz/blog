package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.mapper.UserRoleMapper;
import com.luqiyu.qiyublogspringboot.service.UserRoleService;
import com.luqiyu.qiyublogspringboot.vo.Result;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Api(tags = "用户角色模块")
@RestController
public class UserRoleController {

    @Autowired
    UserRoleService userRoleService;

    @ApiOperation(value = "修改用户角色,修改用户禁用状态")
    @PostMapping("/admin/userRoles")
    public Result changeUserRole(@RequestBody UserRoleVo userRoleVo){
        userRoleService.changeUserRole(userRoleVo);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }
}

