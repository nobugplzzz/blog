package com.luqiyu.qiyublogspringboot.service;

import com.luqiyu.qiyublogspringboot.dto.OperationLogDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-08
 */
public interface OperationLogService extends IService<OperationLog> {
    /**
     * 查询日志列表
     *
     * @param conditionVO 条件
     * @return 日志列表
     */
    PageDTO<OperationLogDTO> listOperationLogs(ConditionVO conditionVO);
}
