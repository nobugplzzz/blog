// 导入Vue ，用于创建Vue实例、使用插件
import Vue from 'vue'
// 导入App.vue组件
import App from './App.vue'
// 导入上面创建的路由配置目录,自动扫描里面的路由配置
import router from './router'
import api from './http/index'
import i18n from './i18n'
import store from './store'
import global from '@/utils/global'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'
import '@/assets/iconfont/iconfont.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.config.productionTip = false

// ElementUI当成Vue插件使用
Vue.use(ElementUI)
// api当成Vue插件使用
Vue.use(api)
Vue.use(mavonEditor)
// 设置Vue全局属性
Vue.prototype.global = global

new Vue({
  // 注入到 vue 对象中
  store,
  router,
  i18n,
  render: h => h(App)
}).$mount('#app')

