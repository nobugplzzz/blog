<template>
  <div class="personal-panel">
    <div class="personal-desc" :style="{'background':this.$store.state.app.themeColor}">
      <div class="avatar-container">
        <img class="avatar" :src="require('@/assets/user.png')">
      </div>
      <div class="name-role">
        <span class="sender">{{ user.nickname }}</span>
        <div class="sender">{{ user.roleList }}</div>
      </div>
      <div class="registe-info">
        <span class="registe-info">
          <li class="fa fa-clock-o" />
          登录时间-{{ user.lastLoginTime }}
        </span>
      </div>
    </div>

    <div class="main-operation">
      <span class="main-operation-item">
        <el-button size="small" icon="fa fa-male" @click="$router.push('/personal/personal')"> 个人中心</el-button>
      </span>
      <span class="main-operation-item">
        <el-button size="small" icon="fa fa-key" @click="$router.push('/personal/personal')"> 修改密码</el-button>
      </span>
    </div>
    <div class="other-operation">
      <div class="other-operation-item">
        <li class="fa fa-user" />
        在线人数
      </div>
      <div class="other-operation-item">
        <li class="fa fa-bell" />
        访问次数
      </div>
    </div>
    <div class="personal-footer" @click="logout">
      <li class="fa fa-sign-out" />
      {{ $t("common.logout") }}
    </div>
  </div>
</template>

<script>

export default {
  name: 'PersonalPanel',
  props: {
    user: {
      type: Object,
      default: () => {
        [
          {
            nickname: 'admin',
            avatar: '@/assets/user.png',
            roleList: '超级管理员',
            lastLoginTime: '登录时间：2018-12-25 '
          }
        ]
      }
    }
  },
  data() {
    return {
      backupVisible: false
    }
  },
  mounted() {
  },
  methods: {
    // 退出登录
    logout: function() {
      this.$confirm('确认退出吗?', '提示', {
        type: 'warning'
      })
        .then(() => {
          sessionStorage.removeItem('userName')
          this.$router.push('/login')
          this.$api.login.logout().then((res) => {
          }).catch(function(res) {
          })
        })
        .catch(() => {})
    }
  }
}
</script>

<style scoped>
.personal-panel {
  font-size: 14px;
  width: 280px;
  text-align: center;
  border-color: rgba(180, 190, 190, 0.2);
  border-width: 1px;
  border-style: solid;
  background: rgba(180, 176, 176, 0.1);
}
.personal-desc {
  background: rgb(19, 138, 156);
  padding: 15px;
  color: #fff;
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 90px;
}
.name-role {
  font-size: 16px;
  padding: 5px;
}
.personal-relation {
  font-size: 16px;
  padding: 12px;
  background: rgba(200, 209, 204, 0.3);
}
.relation-item {
  padding: 12px;
}
.relation-item:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
}
.main-operation {
  padding: 10px;
  /* background: rgba(175, 182, 179, 0.3); */
  border-color: rgba(201, 206, 206, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.main-operation-item {
  margin: 20px;
}
.other-operation {
  padding: 15px;
  text-align: left;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.other-operation-item {
  padding: 12px;
}
.other-operation-item:hover {
  cursor: pointer;
  background: #9e94941e;
  color: rgb(19, 138, 156);
}
.personal-footer {
  font-size: 14px;
  text-align: center;
  padding-top: 10px;
  padding-bottom: 10px;
  border-color: rgba(180, 190, 190, 0.2);
  border-top-width: 1px;
  border-top-style: solid;
}
.personal-footer:hover {
  cursor: pointer;
  color: rgb(19, 138, 156);
  background: #b1a6a61e;
}
</style>
