package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.constant.CommonConst;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserBackDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.luqiyu.qiyublogspringboot.entity.UserRole;
import com.luqiyu.qiyublogspringboot.enums.LoginTypeEnum;
import com.luqiyu.qiyublogspringboot.enums.RoleEnum;
import com.luqiyu.qiyublogspringboot.exception.ServeException;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.mapper.UserRoleMapper;
import com.luqiyu.qiyublogspringboot.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.PasswordVO;
import com.luqiyu.qiyublogspringboot.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    UserAuthMapper userAuthMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public PageDTO<UserBackDTO> listUsersBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 查询文章总数,自己写的sql三表查询
        Long count = userAuthMapper.countUsers(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 查询user,自己写的sql
        List<UserBackDTO> userBackDTOList = userAuthMapper.listUsers(condition);

        return new PageDTO<>(userBackDTOList, count);
    }

    @Override
    public void saveUser(UserVO user) {
        // 校验账号是否合法，包括是否已被注册，验证码是否正确
        if (checkUser(user)) {
            throw new ServeException("邮箱已被注册");
        }
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .email(user.getUsername())
                .nickname(CommonConst.DEFAULT_NICKNAME)
                .avatar(CommonConst.DEFAULT_AVATAR)
                .createTime(new Date())
                .build();
        userInfoMapper.insert(userInfo);
        // 绑定用户角色
        saveRole(userInfo);
        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                // 密码加密，BCrypt方法加密的写法就是这样
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .createTime(new Date())
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();
        userAuthMapper.insert(userAuth);
    }

    /**
     * 校验用户数据是否合法
     *
     * @param user 用户
     * @return
     */
    public boolean checkUser(UserVO user) {
        // 校验验证码是否正确
        // 。。

        // 查询用户名是否存在
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, user.getUsername()));
        // return userAuth != null;
        return Objects.nonNull(userAuth);
    }

    /**
     * 绑定用户角色
     *
     * @param userInfo 用户信息
     */
    public void saveRole(UserInfo userInfo) {
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }
}
