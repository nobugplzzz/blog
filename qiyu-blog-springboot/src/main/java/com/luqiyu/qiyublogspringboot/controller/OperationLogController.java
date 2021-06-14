package com.luqiyu.qiyublogspringboot.controller;


import com.luqiyu.qiyublogspringboot.constant.StatusCodeConst;
import com.luqiyu.qiyublogspringboot.dto.OperationLogDTO;
import com.luqiyu.qiyublogspringboot.dto.PageDTO;
import com.luqiyu.qiyublogspringboot.service.OperationLogService;
import com.luqiyu.qiyublogspringboot.vo.ConditionVO;
import com.luqiyu.qiyublogspringboot.vo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luqiyu
 * @since 2021-06-08
 */
@RestController
public class OperationLogController {

    @Autowired
    OperationLogService operationLogService;

    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation/logs")
    public Result<PageDTO<OperationLogDTO>> listOperationLogs(ConditionVO conditionVO) {
        return new Result<>(true, StatusCodeConst.OK, "查询成功", operationLogService.listOperationLogs(conditionVO));
    }
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/operation/logs")
    public Result deleteOperationLogs(@RequestBody List<Integer> logIdList) {
        operationLogService.removeByIds(logIdList);
        return new Result<>(true, StatusCodeConst.OK, "删除成功");
    }
}

