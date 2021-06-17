import axios from '../axios'

/*
 * 留言管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/message/save',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/message/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const listMessageBackDTO = (data) => {
  return axios({
    url: '/api/admin/messages',
    method: 'get',
    params: data
  })
}
