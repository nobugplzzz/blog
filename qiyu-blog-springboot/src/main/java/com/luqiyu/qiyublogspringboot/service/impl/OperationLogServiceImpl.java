package com.luqiyu.qiyublogspringboot.service.impl;

import com.luqiyu.qiyublogspringboot.entity.OperationLog;
import com.luqiyu.qiyublogspringboot.mapper.OperationLogMapper;
import com.luqiyu.qiyublogspringboot.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-08
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

}
