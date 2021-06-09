package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.luqiyu.qiyublogspringboot.entity.UserRole;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserRoleMapper;
import com.luqiyu.qiyublogspringboot.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public void changeUserRole(UserRoleVo userRoleVo) {
        // 修改用户角色
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVo.getId()));
        UserRole role = UserRole.builder()
                .id(userRole.getId())
                .roleId(userRoleVo.getRoleId())
                .build();
        userRoleMapper.updateById(role);
        // 修改用户状态和昵称
        UserAuth userAuth = UserAuth.builder()
                .id(userRoleVo.getId())
                .isDisabled(userRoleVo.getIsDisabled())
                .build();
        userAuthMapper.updateById(userAuth);
        // 修改用户昵称
        UserAuth auth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, userAuth.getId()));
        UserInfo info = UserInfo.builder()
                .id(auth.getUserInfoId())
                .nickname(userRoleVo.getNickname())
                .build();
        System.out.println(auth);
        System.out.println(info);
        userInfoMapper.updateById(info);
    }
}
