import axios from '../axios'

/*
 * 分类管理模块
 */

// 保存
export const saveOrUpdateCategory = (data) => {
  return axios({
    url: '/api/admin/categories',
    method: 'post',
    data
  })
}
// 删除
export const deleteCategory = (data) => {
  return axios({
    url: '/api/admin/deleteCategory',
    method: 'delete',
    data
  })
}
// 分页查询
export function listCategories(data) {
  return axios({
    url: '/api/admin/categories',
    method: 'get',
    // params是任何一个方法自带的属性，因为这里是使用get，使用params，拼接参数到url就行
    params: data
  })
}
