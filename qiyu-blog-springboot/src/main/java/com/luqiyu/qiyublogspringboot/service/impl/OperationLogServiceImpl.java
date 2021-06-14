package com.luqiyu.qiyublogspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luqiyu.qiyublogspringboot.dto.OperationLogDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.OperationLog;
import com.luqiyu.qiyublogspringboot.mapper.OperationLogMapper;
import com.luqiyu.qiyublogspringboot.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luqiyu.qiyublogspringboot.util.BeanCopyUtil;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public PageDTO<OperationLogDTO> listOperationLogs(ConditionVO conditionVO) {
        Page<OperationLog> page = new Page<>(conditionVO.getCurrent(), conditionVO.getSize());
        // 查询日志列表
        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .gt(Objects.nonNull(conditionVO.getStartTime()), OperationLog::getCreateTime, conditionVO.getStartTime())
                .lt(Objects.nonNull(conditionVO.getEndTime()), OperationLog::getCreateTime, conditionVO.getEndTime())
                .orderByDesc(OperationLog::getId));
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtil.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
        return new PageDTO<>(operationLogDTOList, operationLogPage.getTotal());
    }
}
