import Mock from 'mockjs'
import * as login from './modules/login'
import * as user from './modules/user'
import * as menu from './modules/menu'
import * as dict from './modules/dict'
import * as log from './modules/log'
import * as role from './modules/role'
import * as article from './modules/article'
import * as category from './modules/category'
import * as tag from './modules/tag'
import * as message from './modules/message'
import * as comment from './modules/comment'
import { baseUrl } from '@/utils/global'

// 1. 开启/关闭[所有模块]拦截, 通过调[openMock参数]设置.
// 2. 开启/关闭[业务模块]拦截, 通过调用fnCreate方法[isOpen参数]设置.
// 3. 开启/关闭[业务模块中某个请求]拦截, 通过函数返回对象中的[isOpen属性]设置.

const openMock = true
// const openMock = false

fnCreate(login, openMock)
fnCreate(user, openMock)
fnCreate(menu, openMock)
fnCreate(dict, openMock)
fnCreate(log, openMock)
fnCreate(role, openMock)
fnCreate(article, openMock)
fnCreate(category, false)
fnCreate(tag, false)
fnCreate(message, openMock)
fnCreate(comment, openMock)

/**
 * 创建mock模拟数据
 * @param {*} module 模块
 * @param {*} isOpen 是否开启?
 */
function fnCreate(module, isOpen = true) {
  if (isOpen) {
    for (var key in module) {
      ((res) => {
        if (res.isOpen !== false) {
          let url = baseUrl
          if (!url.endsWith('/')) {
            url = url + '/'
          }
          url = url + res.url
          // RegExp正则表达式，目的是传请求参数，opts是包含了url和参数等信息，options.url 拿到具体的url，options.body 拿到请求参数
          Mock.mock(new RegExp(url), res.type, (options) => {
            // 请求参数
            options['data'] = options.body ? JSON.parse(options.body) : null
            delete options.body
            console.log('\n')
            console.debug('%cmock拦截, 请求: ', 'color:blue', options)
            console.log('%cmock拦截, 响应: ', 'color:blue', res.data)
            return res.data
          })
        }
      })(module[key]() || {})
    }
  }
}
