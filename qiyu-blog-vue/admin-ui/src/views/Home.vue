<template>
  <div class="container">
    <!-- 导航菜单栏 -->
    <NavBar />
    <!-- 头部区域 -->
    <HeadBar />
    <!-- 主内容区域 -->
    <MainContent />
  </div>
</template>
<script>

import HeadBar from './HeadBar/HeadBar'
import NavBar from './NavBar/NavBar'
import MainContent from './MainContent/MainContent'

// es6语法，导出模块本身,Vue-loader再后面做了些什么,相当于导出Vue.component()
// 即导出Home.Vue,name是Home
export default {
  name: 'Home',
  components: {
    HeadBar,
    NavBar,
    MainContent
  },
  data() {
    return {
      isCollapse: false, // 折叠标志
      sysName: 'kitty',
      username: 'Louis',
      userAvatar: '',
      logo: '',
      activeIndex: '1'
    }
  },
  // 钩子函数
  mounted() {
    this.sysName = 'I like Kitty'
    // require()引入
    this.logo = require('@/assets/logo.png')
    var user = sessionStorage.getItem('user')
    if (user) {
      this.userName = user
      // require()引入
      this.userAvatar = require('@/assets/user.png')
    }
  },
  methods: {
    // 侧栏、顶栏菜单的打开选择关闭，element自带
    handleopen() {
      console.log('handleopen')
    },
    handleclose() {
      console.log('handleclose')
    },
    handleselect(a, b) {
      console.log('handleselect')
    },
    handleSelectHearNavBar(key, keyPath) {
      console.log(key, keyPath)
    },
    // 语言切换,@command是指令事件，切换才出发
    handleCommand(command) {
      const array = command.split(':')
      const lang = array[0] === '' ? 'zh_cn' : array[0]
      const label = array[1]
      document.getElementById('language').innerHTML = label
      this.$i18n.locale = lang
    },
    // 折叠导航栏 element自带
    collapse: function() {
      this.isCollapse = !this.isCollapse
    },
    // 退出登录
    logout: function() {
      // var _this = this
      this.$confirm('确认退出吗?', '提示', {
        type: 'warning'
      })
        .then(() => {
          sessionStorage.removeItem('user')
          this.$router.push('/login')
        })
        .catch(() => {})
    }
  }

}

</script>
<style scoped lang="scss">
  .container {
    position:absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    // background: rgba(224, 234, 235, 0.1);
    overflow-y: auto; //滚动条
 }
</style>
