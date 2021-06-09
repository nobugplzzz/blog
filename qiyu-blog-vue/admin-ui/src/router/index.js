import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/Login'
import NotFound from '@/views/Error/404'
import Home from '@/views/Home'
import Intro from '@/views/Intro/Intro'
import api from '@/http/api'
import store from '@/store'
import { getIFramePath, getIFrameUrl } from '@/utils/iframe'
import personal from '@/views/Personal/Personal'

// vue-router是插件，需要use
Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: '首页',
      component: Home,
      children: [
        {
          path: '',
          name: '系统介绍',
          component: Intro,
          meta: {
            icon: 'fa fa-home fa-lg'
          }
        },
        {
          path: '/personal/personal',
          name: '代码生成',
          component: personal,
          meta: {
            icon: 'el-icon-mobile-phone'
          }
        }
      ]
    },
    {
      path: '/login',
      name: '登录',
      component: Login
    },
    {
      path: '/404',
      name: 'notFound',
      component: NotFound
    }
  ]
})
// 导航守卫，相当于检查是否允许跳转路由
router.beforeEach((to, from, next) => {
  // 登录界面登录成功之后，会把用户信息保存在会话
  // 存在时间为会话生命周期，页面关闭即失效。
  const userName = sessionStorage.getItem('user')
  if (to.path === '/login') {
    // 如果是访问登录界面，如果用户会话信息存在，代表已登录过，跳转到主页
    if (userName) {
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    // 如果访问非登录界面，且户会话信息不存在，代表未登录，则跳转到登录界面
    if (!userName) {
      next({ path: '/login' })
    } else {
      // 加载动态菜单和路由,放在导航守卫解决刷新页面vue重新加载后菜单数据为空
      addDynamicMenuAndRoutes(userName, to, from)
      next()
    }
  }
})
/**
* 加载动态菜单和路由
* @userName 用户名，用于菜单权限控制
*/
function addDynamicMenuAndRoutes(userName, to, from) {
  // 处理IFrame嵌套页面
  handleIFrameUrl(to.path)
  // 解决路由守卫重复加载菜单
  if (store.state.app.menuRouteLoaded) {
    console.log('动态菜单和路由已经存在.')
    return
  }
  // 根据登录用户的角色查询后台侧栏菜单
  api.menu.listRoleMenus({ roleId: 1 }).then((res) => {
    // 获取动态路由
    const dynamicRoutes = addDynamicRoutes(res.data)
    router.options.routes[0].children = router.options.routes[0].children.concat(dynamicRoutes)
    // 添加动态路由
    router.addRoutes(router.options.routes)
    // 保存加载状态
    store.commit('menuRouteLoaded', true)
    // 保存菜单树，权限控制
    store.commit('setMenuTree', res.data)
  })
    .then((res) => {
      // 获取并保存用户权限标识集合
      api.user.findPermissions({ 'name': userName }).then((res) => {
        store.commit('setPerms', res.data)
      })
    })
    .catch(function(res) {
      alert(res)
    })
}

/**
 * 处理IFrame嵌套页面
 */
function handleIFrameUrl(path) {
  // 嵌套页面，保存iframeUrl到store，供IFrame组件读取展示
  let url = path
  const length = store.state.iframe.iframeUrls.length
  for (let i = 0; i < length; i++) {
    const iframe = store.state.iframe.iframeUrls[i]
    if (path != null && path.endsWith(iframe.path)) {
      url = iframe.url
      store.commit('setIFrameUrl', url)
      break
    }
  }
}

/**
* 添加动态(菜单)路由
* @param {*} menuList 菜单列表
* @param {*} routes 递归创建的动态(菜单)路由，返回
*/
function addDynamicRoutes(menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].children && menuList[i].children.length >= 1) {
      temp = temp.concat(menuList[i].children)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      // 创建路由配置
      var route = {
        path: menuList[i].url,
        component: null,
        name: menuList[i].name,
        // 自定义路由属性
        meta: {
          icon: menuList[i].icon,
          index: menuList[i].id
        }
      }
      const path = getIFramePath(menuList[i].url)
      if (path) {
        // 如果是嵌套页面, 通过iframe展示
        route['path'] = path
        route['component'] = resolve => require([`@/views/IFrame/IFrame`], resolve)
        // 存储嵌套页面路由路径和访问URL
        const url = getIFrameUrl(menuList[i].url)
        const iFrameUrl = { 'path': path, 'url': url }
        store.commit('addIFrameUrl', iFrameUrl)
      } else {
        try {
          // 根据菜单URL动态加载vue组件，这里要求vue组件须按照url路径存储
          // 如url="sys/user"，则组件路径应是"@/views/sys/user.vue",否则组件加载不到
          // 修改文章带有/*
          menuList[i].url = menuList[i].url.replace('/*', '')
          const array = menuList[i].url.split('/')
          // 导航守卫加载多层
          let url = ''
          for (let i = 0; i < array.length; i++) {
            url += array[i].substring(0, 1).toUpperCase() + array[i].substring(1) + '/'
          }
          url = url.substring(0, url.length - 1)
          route['component'] = resolve => require([`@/views/${url}`], resolve)
        } catch (e) { alert(e) }
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    addDynamicRoutes(temp, routes)
  } else {
    console.log('动态路由加载...')
    console.log(routes)
    console.log('动态路由加载完成.')
  }
  return routes
}
// 导出router
export default router

