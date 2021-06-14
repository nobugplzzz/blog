
export default {
  state: {
    perms: [], // 用户权限标识集合
    userInfo: [] // 登录用户个人中心信息
  },
  getters: {
    getUserInfo(state) {
      return state.userInfo
    }
  },
  mutations: {
    setPerms(state, perms) { // 用户权限标识集合
      state.perms = perms
    },
    setUserInfo(state, userInfo) { // 登录用户个人中心信息
      state.userInfo = userInfo
    }
  },
  actions: {
  }
}
