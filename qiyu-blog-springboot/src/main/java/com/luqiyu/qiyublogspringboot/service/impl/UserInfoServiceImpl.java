package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.luqiyu.qiyublogspringboot.entity.UserRole;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.service.UserRoleService;
import com.luqiyu.qiyublogspringboot.util.UserUtil;
import com.luqiyu.qiyublogspringboot.vo.UserInfoVO;
import com.luqiyu.qiyublogspringboot.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserRoleService userRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(Integer userInfoId, Integer isDisabled) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userInfoId)
                .isDisabled(isDisabled)
                .build();
        userInfoMapper.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname())
                .build();
        userInfoMapper.updateById(userInfo);
        // 删除用户角色重新添加
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        // 流的第二种用法：用列表流的元素获取其实体类列表
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map((roleId) -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId())
                        .build())
                .collect(Collectors.toList());
        // 批量保存
        userRoleService.saveBatch(userRoleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite())
                .updateTime(new Date())
                .build();
        userInfoMapper.updateById(userInfo);
    }
}
