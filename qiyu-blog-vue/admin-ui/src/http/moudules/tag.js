import axios from '../axios'

/*
 * 标签管理模块
 */

// 保存
export const saveOrUpdateTag = (data) => {
  return axios({
    url: '/api/admin/tags',
    method: 'post',
    data
  })
}
// 物理删除
export const deleteTag = (data) => {
  return axios({
    url: '/api/admin/tags',
    method: 'delete',
    data
  })
}
// 分页查询
export const listTags = (data) => {
  return axios({
    url: '/api/admin/tags',
    method: 'get',
    // get方法必须是params，别忘记
    params: data
  })
}
