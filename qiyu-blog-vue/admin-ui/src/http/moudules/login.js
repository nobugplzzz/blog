
import axios from '../axios'

/*
 * 系统登录模块
 */

// 登录
export const login = data => {
  return axios({
    url: '/api/login',
    method: 'post',
    data
  })
}

// 注销
export const logout = () => {
  return axios({
    url: '/api/logout',
    method: 'get'
  })
}
