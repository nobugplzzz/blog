<template>
  <el-submenu v-if="menu.children && menu.children.length >= 1" :index="menu.id + ''">
    <template slot="title">
      <i :class="menu.icon" />
      <span slot="title">{{ menu.name }} </span>
    </template>
    <!-- 递归的子菜单树 -->
    <NavTree v-for="item in menu.children" :key="item.id" :menu="item" />
  </el-submenu>
  <el-menu-item v-else :index="menu.id + ''" :disabled="menu.isDisable === 1 ? true:false" :hidden="menu.isHidden === 1 ? true:false" @click="handleRoute(menu)">
    <i :class="menu.icon" />
    <span slot="title">{{ menu.name }}</span>
  </el-menu-item>
</template>

<script>
import { getIFramePath } from '@/utils/iframe'
import { hasPermission } from '@/permission/index.js'

// 菜单管理的菜单树
export default {
  name: 'NavTree',
  props: {
    menu: {
      type: Object,
      required: true
    }
  },
  methods: {
    handleRoute(menu) {
      // 如果是嵌套页面，转换成iframe的path
      let path = getIFramePath(menu.url)
      if (!path) {
        path = menu.url
      }
      // 通过菜单URL跳转至指定路由,因为path是sys/user形式，少个/ ，或者router.js 过滤菜单的时候不要把前面的/去掉
      this.$router.push('/' + path)
      // alert(path)
    },
    hasPerms: function(perms) {
      // 根据权限标识和外部指示状态进行权限判断
      return hasPermission(perms) & !this.disabled
    }

  }
}
</script>
