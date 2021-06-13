package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 修改用户禁用状态
     *
     * @param userInfoId 用户信息id
     * @param isDisable  禁用状态
     */
    void updateUserDisable(Integer userInfoId, Integer isDisable);

    /**
     * 修改用户权限
     *
     * @param userRoleVO 用户权限
     */
    void updateUserRole(UserRoleVO userRoleVO);
}
