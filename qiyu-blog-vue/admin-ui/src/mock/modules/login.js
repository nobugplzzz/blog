/*
 * 系统登录模块
 */
const loginData = {
  'code': 200,
  'msg': null,
  'data': {
    'id': null,
    'userId': 1,
    'token': '77ae89be36504adfb5c09ef71409ea0e',
    'expireTime': '2018-09-01T16:24:50.473+0000',
    'createBy': null,
    'createTime': null,
    'lastUpdateBy': null,
    'lastUpdateTime': '2018-09-01T04:24:50.473+0000'
  }
}
export function login() {
  return {
    // isOpen: false,
    url: 'sys/login',
    type: 'post',
    data: loginData
  }
}

// 登出接口
export function logout() {
  const logoutData = {
    'code': 200,
    'msg': null,
    'data': {
    }
  }
  return {
    url: 'sys/logout',
    type: 'post',
    data: logoutData
  }
}
