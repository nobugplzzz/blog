import axios from '../axios'

/*
 * 角色管理模块
 */
// 查询全部
export const listRoles = (data) => {
  return axios({
    url: '/api/admin/roles',
    method: 'get',
    params: data
  })
}
// 保存/更新角色
export const saveOrUpdateRole = (data) => {
  return axios({
    url: '/api/admin/roles',
    method: 'put',
    data
  })
}
// 删除
export const deleteRoles = (data) => {
  return axios({
    url: '/api/admin/roles',
    method: 'delete',
    data
  })
}

// 修改角色禁用状态
export const updateRoleDisable = (url, data) => {
  return axios({
    url: url,
    method: 'put',
    data
  })
}
// 查询角色菜单集合

// 保存角色菜单集合

