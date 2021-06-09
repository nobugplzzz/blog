package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.luqiyu.qiyublogspringboot.dto.MenuDTO;
import com.luqiyu.qiyublogspringboot.entity.Article;
import com.luqiyu.qiyublogspringboot.entity.Menu;
import com.luqiyu.qiyublogspringboot.entity.RoleMenu;
import com.luqiyu.qiyublogspringboot.mapper.MenuMapper;
import com.luqiyu.qiyublogspringboot.mapper.RoleMapper;
import com.luqiyu.qiyublogspringboot.mapper.RoleMenuMapper;
import com.luqiyu.qiyublogspringboot.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;


    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO) {
        // 查询菜单
        List<Menu> menus;
        // 带搜索条件 不为空&&不为null
        if (StringUtils.isNotBlank(conditionVO.getKeywords()) && Objects.nonNull(conditionVO.getKeywords())) {
            menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                    .select(Menu::getId, Menu::getName, Menu::getIcon, Menu::getType, Menu::getOrderNum, Menu::getParentId, Menu::getIsDisable, Menu::getIsHidden)
                    .like(Menu::getName, conditionVO.getKeywords()));
            return BeanCopyUtil.copyList(menus, MenuDTO.class);
        }
        // 不带搜索条件
        // 查询树形菜单/节点，思路：查询所有结点相当于查询最高结点（0）的所有子结点
        // 步骤：先查询一级结点，一级结点必是0节点的子结点，再查询每个一级菜单的子菜单
        // 一级菜单列表
        menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getIcon, Menu::getType, Menu::getOrderNum, Menu::getParentId, Menu::getIsDisable, Menu::getIsHidden)
                .eq(Menu::getParentId, 0));
        // 查找子菜单，查询结果直接转DTO
        List<MenuDTO> childMenus = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            Menu currentMenu = menus.get(i);
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setId(currentMenu.getId());
            menuDTO.setName(currentMenu.getName());
            menuDTO.setType(currentMenu.getType());
            menuDTO.setIcon(currentMenu.getIcon());
            menuDTO.setIsDisable(currentMenu.getIsDisable());
            menuDTO.setIsHidden(currentMenu.getIsHidden());
            menuDTO.setParentId(currentMenu.getParentId());
            menuDTO.setChildren(getChildMenu(currentMenu.getId()));
            childMenus.add(menuDTO);
        }
        return childMenus;
    }

    @Override
    public void updateMenuOptions(Integer menuId, Integer isDisable, Integer isHidden) {
        // 修改菜单禁用/隐藏状态
        // builder 构建器 构建对象 更新
        Menu menu = Menu.builder()
                .id(menuId)
                .isDisable(isDisable)
                .isHidden(isHidden)
                .build();

        menuMapper.updateById(menu);
    }

    @Override
    public void saveOrUpdateMenu(MenuVO menuVO) {
        // 添加或修改菜单
        Menu menu = Menu.builder()
                .id(menuVO.getId())
                .name(menuVO.getName())
                .parentId(menuVO.getParentId())
                .url(menuVO.getUrl())
                .orderNum(menuVO.getOrderNum())
                .type(menuVO.getType())
                .icon(menuVO.getIcon())
                .build();

        this.saveOrUpdate(menu);
    }

    @Override
    public void deleteMenus(List<Integer> menuIdList) {
        // 菜单的子菜单
        // List<Menu> childMenuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().in(Menu::getParentId, menuIdList));

        // 删除菜单,菜单的子菜单在前端获取并发送了
        menuMapper.deleteBatchIds(menuIdList);
    }

    @Override
    public List<MenuDTO> listRoleMenus(Integer roleId) {
        // 查询用户的角色，暂时当作roleName，待user_role表完成
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId));
        // 流操作 获取RoleMenu集合的一个MenuId属性集合
        List<Integer> menuIdList = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        // 获取整行menu数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId,0)
                .in(Menu::getId, menuIdList));
        // 递归获取子菜单
        // 查找子菜单，查询结果直接转DTO
        List<MenuDTO> childMenus = new ArrayList<>();
        for (int i = 0; i < menuList.size(); i++) {
            Menu currentMenu = menuList.get(i);
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setId(currentMenu.getId());
            menuDTO.setName(currentMenu.getName());
            menuDTO.setType(currentMenu.getType());
            menuDTO.setIcon(currentMenu.getIcon());
            menuDTO.setIsDisable(currentMenu.getIsDisable());
            menuDTO.setIsHidden(currentMenu.getIsHidden());
            menuDTO.setParentId(currentMenu.getParentId());
            menuDTO.setChildren(getChildMenu(currentMenu.getId()));
            childMenus.add(menuDTO);
        }
        return childMenus;
    }

    /**
     * 递归寻找子节点的方法
     */
    private List<MenuDTO> getChildMenu(Integer id) {
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getUrl, Menu::getType, Menu::getIcon, Menu::getOrderNum, Menu::getParentId, Menu::getIsDisable, Menu::getIsHidden)
                .eq(Menu::getParentId, id));
        // 查询结果直接转DTO
        List<MenuDTO> menuDTOList = BeanCopyUtil.copyList(menuList, MenuDTO.class);
        for (MenuDTO menuDTO : menuDTOList) {
            if (menuDTO.getParentId() != null) {
                menuDTO.setChildren(getChildMenu(menuDTO.getId()));
            }
        }
        return menuDTOList;
    }
}