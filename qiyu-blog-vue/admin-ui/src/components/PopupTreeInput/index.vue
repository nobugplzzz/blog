<template>
  <div>
    <!-- 弹出机构名 -->
    <el-popover ref="popover" :placement="placement" trigger="click">
      <!-- 机构名树形展示 -->
      <el-tree
        ref="popupTree"
        :data="data"
        :props="props"
        node-key="nodeKey"
        :default-expand-all="defaultExpandAll"
        :highlight-current="true"
        :expand-on-click-node="true"
        @current-change="currentChangeHandle"
      />
    </el-popover>
    <el-input v-model="prop" v-popover:popover :readonly="true" :placeholder="placeholder" style="cursor:pointer;" />
  </div>
</template>

<script>

export default {
  name: 'PopupTreeInput',
  props: {
    // 展示数据
    data: {
      type: Array,
      default: () => []
    },
    // 	配置选项
    props: {
      type: Object,
      default: function() {
        return {}
      }
    },
    prop: {
      type: String,
      default: ''
    },
    nodeKey: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '点击选择内容'
    },
    placement: {
      type: String,
      default: 'right-start'
    },
    currentChangeHandle: {
      type: Function,
      default: null
    },
    defaultExpandAll: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style scoped>

</style>
