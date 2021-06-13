package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserRoleDTO;
import com.luqiyu.qiyublogspringboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.RoleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-06
 */
public interface RoleService extends IService<Role> {

    /**
     * 查询角色列表
     * @param conditionVO 查询条件
     * @return 角色列表
     */
    PageDTO<Role> listRoles (ConditionVO conditionVO);

    /**
     * 新建或更新角色
     * @param roleVO 角色视图对象
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除/批量删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);

    /**
     * 更新角色禁用状态
     * @param roleId 角色id
     */
    void updateRoleDisable(Integer roleId,Integer isDisable);

    /**
     * 编辑用户时查询用户角色选项
     *
     * @return 角色选项列表
     */
    List<UserRoleDTO> listUserRoles();
}
