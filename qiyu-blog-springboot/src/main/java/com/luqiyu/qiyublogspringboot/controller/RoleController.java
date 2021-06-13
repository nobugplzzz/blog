package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserRoleDTO;
import com.luqiyu.qiyublogspringboot.entity.Role;
import com.luqiyu.qiyublogspringboot.service.RoleService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import com.luqiyu.qiyublogspringboot.vo.RoleVO;
import io.swagger.annotations.Api;
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
 * @since 2021-06-06
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @ApiOperation(value = "角色管理页面查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageDTO<Role>> listRoles(ConditionVO conditionVO){
        return new Result<>(true, StatusCodeConst.OK, "操作成功",roleService.listRoles(conditionVO));
    }

    @ApiOperation(value = "新建或更新角色")
    @PutMapping("/admin/roles")
    public Result saveOrUpdateRole(@RequestBody RoleVO roleVO){
        roleService.saveOrUpdateRole(roleVO);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result deleteRoles(@RequestBody List<Integer> roleIdList){
        roleService.deleteRoles(roleIdList);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "更新角色禁用状态")
    @PutMapping("/admin/roles/disable/{roleId}")
    public Result updateRoleDisable(@PathVariable("roleId") @RequestBody Integer roleId,Integer isDisable){
        roleService.updateRoleDisable(roleId,isDisable);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "编辑用户时查询用户角色选项列表")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles() {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", roleService.listUserRoles());
    }
}

