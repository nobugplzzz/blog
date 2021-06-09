package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.MenuDTO;
import com.luqiyu.qiyublogspringboot.mapper.MenuMapper;
import com.luqiyu.qiyublogspringboot.service.MenuService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MenuVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @since 2021-06-03
 */
@Api(tags="菜单模块")
@RestController
public class MenuController {

    @Autowired
    MenuService menuService;

    @ApiOperation(value = "查询后台菜单（菜单管理）")
    @GetMapping("/admin/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", menuService.listMenus(conditionVO));
    }

    @ApiOperation(value = "修改菜单禁用/隐藏")
    //用在 @ApiImplicitParams 注解中，指定一个请求参数的配置信息
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isDisable", value = "是否禁用", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "isHidden", value = "是否隐藏", required = true, dataType = "Long")
    })
    @PutMapping("/admin/menu/options/{articleId}")
    public Result updateMenuOptions(@PathVariable("articleId") Integer menuId, Integer isDisable, Integer isHidden) {
        menuService.updateMenuOptions(menuId, isDisable, isHidden);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "添加或更新菜单")
    @PutMapping("/admin/menus")
    public Result saveOrUpdateMenu(@RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "删除菜单及其子菜单")
    @DeleteMapping("/admin/menus")
    public Result deleteMenus( @RequestBody List<Integer> menuIdList) {
        menuService.deleteMenus(menuIdList);
        return new Result<>(true, StatusCodeConst.OK, "操作成功");
    }

    @ApiOperation(value = "根据登录用户的角色查询后台侧栏菜单")
    @GetMapping("/admin/role/menus")
    public Result<List<MenuDTO>> listRoleMenus(Integer roleId) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", menuService.listRoleMenus(roleId));
    }
}

