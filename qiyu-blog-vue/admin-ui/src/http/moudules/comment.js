import axios from '../axios'

/*
 * 评论管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/comment/save',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/comment/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/comment/findPage',
    method: 'post',
    data
  })
}
