<template>
  <div class="head-bar-container" :class="collapse ? 'menu-bar-collapse-width':'menu-bar-width'">
    <!-- 导航收缩 -->
    <span class="hamburger-container">
      <Hamburger :toggle-click="onCollapse" :is-active="$store.state.app.collapse" />
    </span>
    <!-- 导航菜单 -->
    <span class="nav-bar">
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        text-color="#fff"
        active-text-color="#ffd04b"
        background-color="#545c64"
        mode="horizontal"
        @select="selectNavBar()"
      >
        <el-menu-item index="1" style="font-size: 16px;" @click="$router.push('/')"><i class="fa fa-home fa-lg" /> {{ $t("common.home") }}</el-menu-item>
        <el-menu-item index="2" style="font-size: 16px;" @click="openWindow('https://www.cnblogs.com/xifengxiaoma/')"><i class="kt-iconego-blog" /> {{ $t("common.blog") }}</el-menu-item>

      </el-menu>
    </span>
    <span class="tool-bar">
      <el-menu
        class="el-menu-demo"
        text-color="#fff"
        active-text-color="#ffd04b"
        background-color="#545c64"
        mode="horizontal"
      >
        <el-menu-item v-popover:popover-lang index="1">
          <!-- 语言切换 -->
          <LangSelector />
        </el-menu-item>
        <el-menu-item v-popover:popover-personal index="5">
          <!-- 用户信息 -->
          <span class="user-info"><img :src="user.avatar">{{ user.name }}</span>
          <el-popover ref="popover-personal" placement="bottom-end" trigger="hover" :visible-arrow="false">
            <PersonalPanel :user="user" />
          </el-popover>
        </el-menu-item>
      </el-menu>
    </span>
  </div>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import mock from '@/mock/index.js'
import LangSelector from '@/components/LangSelector'
import Hamburger from '@/components/Hamburger'
import { mapState } from 'vuex'
import PersonalPanel from '@/views/Core/PersonalPanel'

export default {
  // 使用的组件
  components: {
    // ComponentA: ComponentA的缩写
    Hamburger,
    LangSelector,
    PersonalPanel
  },
  data() {
    return {
      user: {
        name: 'Louis',
        avatar: '',
        role: '超级管理员',
        registeInfo: '注册时间：2018-12-20 '
      },
      activeIndex: '1',
      langVisible: false
    }
  },
  computed: {
    ...mapState({
      collapse: state => state.app.collapse
    })
  },
  // 钩子函数
  mounted() {
    // this.sysName = 'I like Kitty'
    var user = sessionStorage.getItem('user')
    if (user) {
      this.user.name = user
      this.user.avatar = require('@/assets/user.png')
    }
  },
  methods: {
    openWindow(url) {
      window.open(url)
    },
    // elementUI
    selectNavBar(key, keyPath) {
      console.log(key, keyPath)
    },
    // 语言切换，@command是指令事件，切换才触发
    handleCommand(command) {
      const array = command.split(':')
      const lang = array[0] === '' ? 'zh_cn' : array[0]
      const label = array[1]
      document.getElementById('language').innerHTML = label
      this.$i18n.locale = lang
    },
    // 折叠导航栏
    onCollapse: function() {
      this.$store.commit('onCollapse') // 唤醒一个名为‘onCollapse’的 mutation handler
    }

  }
}
</script>

<style scoped lang="scss">
.head-bar-container  {
  position: absolute;
  left: 200px;
  right: 0px;
  height: 60px;
  line-height: 60px;
   background: #545c64;
  .hamburger-container {
    width: 40px;
    float: left;
    border-color: rgba(238, 241, 241, 0.4);
    border-left-width: 1px;
    border-left-style: solid;
    border-right-width: 1px;
    border-right-style: solid;
    color: white;
  }
  .nav-bar {
    margin-left: auto;
    float: left;
    .el-menu {
      background: #0a463480;
    }
  }
  .tool-bar {
    float: right;
    .theme-picker {
      padding-right: 10px;

    }
    .user-info {
      font-size: 16px;
      padding-right: 16px;
      color: #fff;
      cursor: pointer;
      img {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        margin: 10px 0px 10px 10px;
        float: right;
      }
    }
  }
}
.badge {

  line-height: 18px;
}
.menu-bar-width {
  left: 200px;
}
.menu-bar-collapse-width {
  left: 65px;
}
</style>
