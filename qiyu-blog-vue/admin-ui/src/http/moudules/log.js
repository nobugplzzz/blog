import axios from '../axios'

/*
 * 日志管理模块
 */

// 查询操作日志
export const listOperationLogs = (data) => {
  return axios({
    url: '/api/admin/operation/logs',
    method: 'get',
    params: data
  })
}
