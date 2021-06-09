<template>
  <div class="nav-bar-container">
    <!-- logo -->
    <div class="logo" :class="collapse?'nav-bar-collapse-width':'nav-bar-width'" @click="$router.push('/')">
      <img v-if="collapse" src="@/assets/logo.png">
      <div>{{ collapse?'':appName }}</div>
    </div>
    <!-- 导航菜单 -->
    <el-menu
      :default-active="index+''"
      :class="collapse?'nav-bar-collapse-width':'nav-bar-width'"
      :collapse="collapse"
      :collapse-transition="false"
      :unique-opened="false"
      @open="handleopen"
      @close="handleclose"
      @select="handleselect"
    >
      <!-- 导航菜单树组件，动态加载菜单,遍历保存在store的菜单数据 -->
      <NavTree v-for="item in navTree" :key="item.id" :menu="item" />
    </el-menu>
  </div>
</template>

<script>
//  引入mapState（映射state）设置state别名
import { mapState } from 'vuex'
import NavTree from '@/components/NavTree/index'

export default {
  components: {
    NavTree
  },
  data() {
    return {
      index: this.$route.meta.index
    }
  },
  // 计算属性
  computed: {
    // 数据/状态共享
    ...mapState({
      // 应用名称，不需要$store.
      appName: state => state.app.appName,
      // MenuBar展开收缩状态
      collapse: state => state.app.collapse,
      // 菜单树数据
      navTree: state => state.menu.navTree
    }),
    mainTabs: {
      get() { return this.$store.state.tab.mainTabs },
      set(val) { this.$store.commit('updateMainTabs', val) }
    },
    mainTabsActiveName: {
      get() { return this.$store.state.tab.mainTabsActiveName },
      set(val) { this.$store.commit('updateMainTabsActiveName', val) }
    }
  },
  // 观察路由变化
  watch: {
    $route: 'handleRoute'
  },
  // 配合watch，created生命周期设置路由,刷新页面保存最后关闭时的路由
  created() {
    this.handleRoute(this.$route)
  },

  methods: {
    handleopen: function() {
      console.log('handleopen')
    },
    handleclose: function() {
      console.log('handleclose')
    },
    handleselect: function(a, b) {
      console.log('handleselect')
    },
    // 路由标签操作处理
    handleRoute: function(route) {
      // tab标签页选中, 如果不存在则先添加
      var tab = this.mainTabs.filter(item => item.name === route.name)[0]
      if (!tab) {
        tab = {
          name: route.name,
          title: route.name,
          icon: route.meta.icon
        }
        this.mainTabs = this.mainTabs.concat(tab)
      }
      this.mainTabsActiveName = tab.name
    }

  }
}
</script>

<style scoped lang="scss">
.nav-bar-container {
  .el-menu {
    position:absolute;
    top: 60px;
    bottom: 0px;
    text-align: left;
  }
  .logo {
    position:absolute;
    top: 0px;
    height: 60px;
    line-height: 60px;
    background: #467366;
     cursor:pointer;
    img {
        width: 40px;
        height: 40px;
        border-radius: 0px;
        margin: 10px 10px 10px 10px;
        float: left;
    }
    div {
      font-size: 25px;
      color: white;
      text-align: center;
      // padding-left: 20px;
    }
  }
  .nav-bar-width {
    width: 200px;
  }
  .nav-bar-collapse-width {
    width: 65px;
  }
}
</style>
