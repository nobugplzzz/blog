<template>
  <div id="main-container" class="main-container" :class="$store.state.app.collapse ? 'menu-bar-collapse-width':'menu-bar-width'">
    <!-- 标签页 -->
    <div class="tab-container">
      <el-tabs
        v-model="mainTabsActiveName"
        class="tabs"
        :closable="true"
        type="card"
        @tab-click="selectedTabHandle"
        @tab-remove="removeTabHandle"
      >
        <el-tab-pane
          v-for="item in mainTabs"
          :key="item.name"
          :label="item.title"
          :name="item.name"
        >
          <span slot="label"><i :class="item.icon" /> {{ item.title }} </span>
        </el-tab-pane>
      </el-tabs>
      <!-- 标签页选项 -->
      <el-dropdown class="tabs-tools" :show-timeout="0" trigger="click">
        <el-button type="primary" size="small" style="font-size: 14px;"><i class="el-icon-arrow-down" /> 标签选项</el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="tabsCloseCurrentHandle">关闭当前标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseOtherHandle">关闭其它标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsCloseAllHandle">关闭全部标签页</el-dropdown-item>
          <el-dropdown-item @click.native="tabsRefreshCurrentHandle">刷新当前标签页</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!-- 主内容区域 -->
    <div class="main-content">
      <keep-alive>
        <transition name="fade" mode="out-in">
          <!-- 显示路由视图 -->
          <router-view />
        </transition>
      </keep-alive>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
    }
  },
  computed: {
    // 全部标签
    mainTabs: {
      get() { return this.$store.state.tab.mainTabs },
      set(val) { this.$store.commit('updateMainTabs', val) }
    },
    // 当前标签
    mainTabsActiveName: {
      get() { return this.$store.state.tab.mainTabsActiveName },
      set(val) { this.$store.commit('updateMainTabsActiveName', val) }
    }
  },
  methods: {
    // tabs, 选中tab
    selectedTabHandle(tab) {
      tab = this.mainTabs.filter(item => item.name === tab.name)
      if (tab.length >= 1) {
        this.$router.push({ name: tab[0].name })
      }
    },
    // tabs, 删除tab
    removeTabHandle(tabName) {
      this.mainTabs = this.mainTabs.filter(item => item.name !== tabName)
      if (this.mainTabs.length >= 1) {
        // 当前选中tab被删除
        if (tabName === this.mainTabsActiveName) {
          this.$router.push({ name: this.mainTabs[this.mainTabs.length - 1].name }, () => {
            this.mainTabsActiveName = this.$route.name
          })
        }
      } else {
        this.$router.push('/')
      }
    },
    // tabs, 关闭当前
    tabsCloseCurrentHandle() {
      this.removeTabHandle(this.mainTabsActiveName)
    },
    // tabs, 关闭其它
    tabsCloseOtherHandle() {
      this.mainTabs = this.mainTabs.filter(item => item.name === this.mainTabsActiveName)
    },
    // tabs, 关闭全部
    tabsCloseAllHandle() {
      this.mainTabs = []
      this.$router.push('/')
    },
    // tabs, 刷新当前
    tabsRefreshCurrentHandle() {
      var tempTabName = this.mainTabsActiveName
      this.removeTabHandle(tempTabName)
      this.$nextTick(() => {
        this.$router.push({ name: tempTabName })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.main-container {
  padding: 0 5px 5px;
  position: absolute;
  top: 60px;
  right: 0px;
  border-color: rgba(148, 145, 147, 0.2);
  border-left-width: 1px;
  border-left-style: solid;
    .tabs {
    width:88%;
    padding: 2px;;
    font-size: 14px;
    background: rgb(255, 253, 255);
    margin-top: 5px;
      .tabs-tools {
      float: right;
    }
  }
  .tabs-tools{
  float: right;
  top: -55px;
  }
  .main-content {
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    height:auto;
  }
    .fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
  }
}
  .menu-bar-collapse-width{
     left: 65px;
  }
  .menu-bar-width{
    left: 200px;
  }

</style>
