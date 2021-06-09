package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.Role;
import com.luqiyu.qiyublogspringboot.mapper.RoleMapper;
import com.luqiyu.qiyublogspringboot.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.RoleVO;
import javafx.beans.property.ReadOnlyListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleService roleService;

    @Override
    public PageDTO<Role> listRoles(ConditionVO conditionVO) {
        Page<Role> page = new Page<>(conditionVO.getCurrent(), conditionVO.getSize());

        Page<Role> rolePage = roleMapper.selectPage(page, new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName, Role::getRoleLabel, Role::getRoleRemark,Role::getIsDisable, Role::getCreateTime)
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords())
                .orderByAsc(Role::getId));

        PageDTO<Role> rolePageDTO = new PageDTO<>(rolePage.getRecords(), rolePage.getTotal());
        return rolePageDTO;
    }

    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .roleRemark(roleVO.getRoleRemark())
                .build();
        roleService.saveOrUpdate(role);
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        roleMapper.deleteBatchIds(roleIdList);
    }

    @Override
    public void updateRoleDisable(Integer roleId,Integer isDisable) {
        Role role = Role.builder()
                .id(roleId)
                .isDisable(isDisable)
                .build();

        roleMapper.updateById(role);
    }
}
