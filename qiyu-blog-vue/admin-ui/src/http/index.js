// 导入所有接口
import apis from './api'

// 正常写法：install=function(Vue){}
const install = Vue => {
  if (install.installed) { return }

  install.installed = true

  Object.defineProperties(Vue.prototype, {
    // 注意，此处挂载在 Vue 原型的 $api 对象上，如何调用？-this.$api
    $api: {
      get() {
        return apis
      }
    }
  })
}

export default install
// export as namespace api;
