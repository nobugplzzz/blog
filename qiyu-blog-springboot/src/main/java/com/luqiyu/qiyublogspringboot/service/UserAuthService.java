package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserBackDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.PasswordVO;
import com.luqiyu.qiyublogspringboot.vo.UserVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
public interface UserAuthService extends IService<UserAuth> {


    /**
     * 查询后台用户列表
     *
     * @param conditionVO 查询条件
     * @return
     */
    PageDTO<UserBackDTO> listUsersBackDTO(ConditionVO conditionVO);

    /**
     * 用户注册
     * @param user 注册用户对象
     */
    void saveUser(UserVO user);

    /**
     * 修改密码
     *
     * @param user 用户对象
     */
    void updatePassword(UserVO user);

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVO passwordVO);
}
