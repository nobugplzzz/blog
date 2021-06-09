package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.dto.UserBackDTO;
import com.luqiyu.qiyublogspringboot.entity.UserAuth;
import com.luqiyu.qiyublogspringboot.exception.ServeException;
import com.luqiyu.qiyublogspringboot.mapper.UserAuthMapper;
import com.luqiyu.qiyublogspringboot.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.PasswordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-07
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    UserAuthMapper userAuthMapper;

    @Override
    public PageDTO<UserBackDTO> listUsersBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent()-1)*condition.getSize());
        // 查询文章总数,自己写的sql三表查询
        Long count = userAuthMapper.countUsers(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 查询user,自己写的sql
        List<UserBackDTO> userBackDTOList = userAuthMapper.listUsers(condition);

        return new PageDTO<>(userBackDTOList,count);
    }


}
