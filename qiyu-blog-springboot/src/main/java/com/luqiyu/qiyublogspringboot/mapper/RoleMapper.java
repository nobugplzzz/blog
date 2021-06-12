package com.luqiyu.qiyublogspringboot.mapper;

import com.luqiyu.qiyublogspringboot.dto.UrlRoleDTO;
import com.luqiyu.qiyublogspringboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-06
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取用户角色标签列表
     *
     * @param userInfoId 用户id
     * @return 角色标签列表
     */
    List<String> listRoleByUserInfoId(Integer userInfoId);

    /**
     * 获取接口url资源的角色列表
     * 根据UrlRoleDTO的属性，要查三个表，资源表，角色表，资源角色中间表
     *
     * @return UrlRoleDTO列表
     */
    List<UrlRoleDTO> listUrlRoles();
}
