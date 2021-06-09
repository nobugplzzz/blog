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
export const changeUserRole = (data) => {
  return axios({
    url: '/api/admin/userRoles',
    method: 'post',
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
