import axios from '../axios'

/*
 * 菜单管理模块
 */

// 查找菜单管理菜单树
export const listMenus = (data) => {
  return axios({
    url: 'api/admin/menus',
    method: 'get',
    params: data
  })
}

// 修改菜单禁用/隐藏
export const updateMenuOptions = (url, data) => {
  return axios({
    url: url,
    method: 'put',
    data
  })
}

// 添加/更新菜单
export const saveOrUpdateMenu = (data) => {
  return axios({
    url: '/api/admin/menus',
    method: 'put',
    data
  })
}

// 删除菜单
export const deleteMenus = (data) => {
  return axios({
    url: '/api/admin/menus',
    method: 'delete',
    data
  })
}

// 根据登录用户的角色查询后台侧栏菜单
export const listRoleMenus = (data) => {
  return axios({
    url: 'api/admin/role/menus',
    method: 'get',
    params: data
  })
}
