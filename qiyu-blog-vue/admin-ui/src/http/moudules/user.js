import axios from '../axios'

/*
 * 用户管理模块
 */

// 查询后台用户列表
export const listUsers = (data) => {
  return axios({
    url: '/api/admin/users',
    method: 'get',
    params: data
  })
}

// 查询后台用户列表
export const updateUserRole = (data) => {
  return axios({
    url: '/api/admin/users/role',
    method: 'put',
    data
  })
}

// 查找用户的菜单权限标识集合
export const findPermissions = (params) => {
  return axios({
    url: '/user/findPermissions',
    method: 'get',
    params
  })
}

// 修改用户是否禁用
export const updateUserSilence = (url, data) => {
  return axios({
    url: url,
    method: 'put',
    data
  })
}
