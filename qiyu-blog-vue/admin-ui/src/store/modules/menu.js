export default ({
  state: {
    navTree: [] // 菜单树
  },
  getters: {

  },
  mutations: {
    setMenuTree(state, navTree) { // 设置菜单树
      state.navTree = navTree
    }
  },
  actions: {}
})
