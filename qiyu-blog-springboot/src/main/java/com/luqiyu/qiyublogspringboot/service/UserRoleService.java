package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 修改用户角色
     * @param userRoleVo 用户角色视图对象
     */
    void changeUserRole(UserRoleVo userRoleVo);
}
