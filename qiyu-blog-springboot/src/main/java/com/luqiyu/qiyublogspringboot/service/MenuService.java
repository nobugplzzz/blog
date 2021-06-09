package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.MenuDTO;
import com.luqiyu.qiyublogspringboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.CategoryVO;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MenuVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-03
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据登录用户的角色查询后台侧栏菜单
     * @param conditionVO 查询条件
     * @return
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 修改菜单禁用/隐藏
     * @param menuId 菜单id
     * @param isDisable 是否禁用
     * @param isHidden 是否隐藏
     */
    void updateMenuOptions( Integer menuId, Integer isDisable, Integer isHidden);

    /**
     * 添加或更新菜单
     * @param menuVO 菜单视图对象
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     * @param menuIdList 菜单列表
     */
    void deleteMenus(List<Integer> menuIdList);

    /**
     * 根据登录用户的角色查询后台侧栏菜单
     * @param roleId 角色id
     * @return
     */
    List<MenuDTO> listRoleMenus(Integer roleId);
}
