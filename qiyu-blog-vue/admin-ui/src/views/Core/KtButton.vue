<template>
  <el-button
    class="ktbutton"
    :style="display"
    :size="size"
    :type="type"
    :loading="loading"
    :disabled="!hasPerms(perms)"
    :icon="icon"
    @click="handleClick"
  >
    <!-- 父子组件通信，父传递label值到子 -->
    {{ label }}
  </el-button>
</template>

<script>
import { hasPermission } from '@/permission/index.js'

export default {
  name: 'KtButton',
  props: {
    label: { // 按钮显示文本
      type: String,
      default: 'Button'
    },
    icon: { // 按钮显示图标
      type: String,
      default: ''
    },
    size: { // 按钮尺寸
      type: String,
      default: 'small'
    },
    type: { // 按钮类型
      type: String,
      default: null
    },
    loading: { // 按钮加载标识
      type: Boolean,
      default: false
    },
    disabled: { // 按钮是否禁用
      type: Boolean,
      default: false
    },
    perms: { // 按钮权限标识，外部使用者传入
      type: String,
      default: null
    },
    display: {
      type: String,
      default: null
    }
  },
  data() {
    return {
    }
  },
  mounted() {
  },
  methods: {
    handleClick: function() {
      // 按钮操作处理函数,新增或编辑？
      this.$emit('click', {})
    },
    hasPerms: function(perms) {
      // 根据权限标识和外部指示状态进行权限判断
      return hasPermission(perms) & !this.disabled
    }
  }
}
</script>

<style scoped>
.ktbutton{
  font-size: 14px;

}
</style>
