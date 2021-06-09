
<template>
  <!--  Login.vue 的模板 -->
  <el-form ref="loginForm" :model="loginForm" :rules="fieldRules" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <span class="tool-bar">
      <!-- 语言切换 -->
      <lang-selector class="lang-selector" />
    </span>
    <h2 class="title" style="padding-left:22px;">系统登录</h2>
    <el-form-item prop="account">
      <el-input v-model="loginForm.account" type="text" auto-complete="off" placeholder="账号" />
    </el-form-item>
    <el-form-item prop="password">
      <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" />
    </el-form-item>
    <!-- 验证码 -->
    <el-form-item>
      <el-col :span="12">
        <el-form-item prop="captcha">
          <el-input
            v-model="loginForm.captcha"
            type="test"
            auto-complete="off"
            placeholder="验证码, 单击图片刷新"
            style="width: 100%;"
          />
        </el-form-item>
      </el-col>
      <el-col class="line" :span="1">&nbsp;</el-col>
      <el-col :span="11">
        <el-form-item>
          <img style="width: 100%;" class="pointer" :src="loginForm.src" @click="refreshCaptcha">
        </el-form-item>
      </el-col>
    </el-form-item>
    <!-- <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox> -->
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:48%;" @click.native.prevent="reset">重 置</el-button>
      <el-button type="primary" style="width:48%;" :loading="loading" @click="login()">登 录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>

// es6语法，导出模块本身,Vue-loader再后面做了些什么,相当于导出Vue.componen()
// 即导出Login.Vue,name是Login
import Cookies from 'js-cookie'
import LangSelector from '@/components/LangSelector'

export default {
  name: 'Login',
  components: {
    LangSelector
  },
  data() {
    return {
      loading: false,
      loginForm: {
        account: 'admin',
        password: 'admin',
        captcha: '', // 验证码
        src: ''
      },
      fieldRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      checked: true
    }
  },
  mounted() {
    this.refreshCaptcha()
  },
  methods: {
    login() {
      this.loading = true
      const userInfo = { account: this.loginForm.account, password: this.loginForm.password, captcha: this.loginForm.captcha }
      this.$api.login.login(userInfo).then((res) => {
        if (res.msg != null) {
          this.$message({
            message: res.msg,
            type: 'error'
          })
        } else {
          Cookies.set('token', res.data.token) // 放置token到Cookie
          sessionStorage.setItem('user', userInfo.account) // 保存用户到本地会话
          this.$store.commit('menuRouteLoaded', false) // 要求重新加载导航菜单
          this.$router.push('/') // 登录成功，跳转到主页
        }
        this.loading = false
      }).catch((res) => {
        this.$message({
          message: res.message,
          type: 'error'
        })
      })
    },
    refreshCaptcha: function() {
      this.loginForm.src = this.global.baseUrl + '/captcha.jpg?t=' + new Date().getTime()
    },
    reset() {
      this.$refs.loginForm.resetFields()
    }

  }

}
</script>

<style lang="scss" scoped>
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 40px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>
