<template>
  <!--通过其$attrs可以获取到父组件传过来没有使用props注册的属性 -->
  <el-table-column :prop="prop" v-bind="$attrs">
    <template slot-scope="scope">
      <span :style="childStyles(scope.$index,scope.row)" @click.prevent="toggleHandle(scope.$index, scope.row)">
        <i :class="iconClasses(scope.row)" :style="iconStyles(scope.row)" />
        {{ scope.row[prop] }}
      </span>
    </template>
  </el-table-column>
</template>

<script>
import isArray from 'lodash/isArray'

// 菜单管理的“名称”属性，做一个树形组件
export default {
  name: 'TableTreeColumn',
  props: {
    prop: {
      type: String, // 单选框
      default: ''
    },
    treeKey: {
      type: String,
      default: 'id'
    },
    parentKey: {
      type: String,
      default: 'parentId'
    },
    levelKey: {
      type: String,
      default: 'level'
    },
    childKey: {
      type: String,
      default: 'children'
    }
  },
  methods: {
    // 子菜单css
    childStyles($index, row) {
      // alert(row[this.parentKey])
      if (row[this.parentKey] !== 0) {
        return { 'padding-left': 25 + 'px' }
      }
      return null
    },
    // 三角形展开收缩标志方向
    iconClasses(row) {
      return [!row._expanded ? 'el-icon-caret-right' : 'el-icon-caret-bottom']
    },
    // 三角形展开收缩标志是否显示
    iconStyles(row) {
      return { 'visibility': this.hasChild(row) ? 'visible' : 'hidden' }
    },
    hasChild(row) {
      return (isArray(row[this.childKey]) && row[this.childKey].length >= 1) || false
    },
    // 切换处理
    toggleHandle(index, row) {
      if (this.hasChild(row)) {
        var data = this.$parent.store.states.data.slice(0)
        data[index]._expanded = !data[index]._expanded
        if (data[index]._expanded) {
          data = data.splice(0, index + 1).concat(row[this.childKey]).concat(data)
        } else {
          data = this.removeChildNode(data, row[this.treeKey])
        }
        this.$parent.store.commit('setData', data)
        this.$nextTick(() => {
          this.$parent.doLayout()
        })
      }
    },
    // 移除子节点
    removeChildNode(data, parentId) {
      var parentIds = isArray(parentId) ? parentId : [parentId]
      if (parentId.length <= 0) {
        return data
      }
      var ids = []
      for (var i = 0; i < data.length; i++) {
        if (parentIds.indexOf(data[i][this.parentKey]) !== -1 && parentIds.indexOf(data[i][this.treeKey]) === -1) {
          ids.push(data.splice(i, 1)[0][this.treeKey])
          i--
        }
      }
      return this.removeChildNode(data, ids)
    }
  }
}
</script>
