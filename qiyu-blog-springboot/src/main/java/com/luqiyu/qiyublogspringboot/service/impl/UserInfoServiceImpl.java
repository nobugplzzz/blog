package com.luqiyu.qiyublogspringboot.service.impl;

import com.luqiyu.qiyublogspringboot.entity.UserInfo;
import com.luqiyu.qiyublogspringboot.mapper.UserInfoMapper;
import com.luqiyu.qiyublogspringboot.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
