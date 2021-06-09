<template>
  <div class="KtTable">
    <!--表格栏 :data="data.recordList"第一个data是el-table自身属性；第二个data是props的data，用于父向子组件传值，传过来pageResult就是data-->
    <el-table
      class="table"
      :data="data.recordList"
      :border="border"
      :stripe="stripe"
      :highlight-current-row="highlightCurrentRow"
      :v-loading="loading"
      element-loading-text="拼命加载中"
      :max-height="maxHeight"
      :size="size"
      :align="align"
      style="width:100%;"
      @current-change="handleCurrentChange"
      @selection-change="selectionChange"
    >
      <el-table-column v-if="showOperation" type="selection" width="40" />
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        header-align="left"
        align="left"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        :fixed="column.fixed"
        :type="column.type"
        :sortable="column.sortable==null?true:column.sortable"
        :formatter="column.formatter"
      />
      <el-table-column v-if="showOperation" header-align="center" align="center" :label="$t('action.operation')" width="200" fixed="right">
        <!--编辑删除按钮， 插槽，用于父组件（User）访问子组件（KtTable）的内容，elementUI就是这么写-->
        <template v-slot="scope">
          <KtButton icon="fa fa-edit" :label="$t('action.edit')" :perms="permsEdit" :display="editDisplayNone" :size="size" @click="handleEdit(scope.$index, scope.row)" />
          <KtButton v-if="showBatchDelete & showOperation" icon="fa fa-trash" :label="$t('action.delete')" :perms="permsDelete" :size="size" type="danger" @click="handleDelete(scope.$index, scope.row)" />
        </template>
      </el-table-column>
    </el-table>
    <!--分页栏-->
    <div class="toolbar" style="padding:10px;">
      <KtButton
        v-if="showBatchDelete & showOperation"
        label="批量删除"
        :perms="permsDelete"
        :size="size"
        type="danger"
        :disabled="selections.length===0"
        style="float:left;"
        @click="handleBatchDelete()"
      />
      <el-pagination
        layout="total,sizes, prev, pager, next, jumper"
        :current-page="pageRequest.current"
        :page-size="pageRequest.size"
        :page-sizes="[2, 3, 4, 10]"
        :total="data.count"
        style="float:right;"
        @size-change="pageSizeChange"
        @current-change="currentPageChange"
      />
    </div>
  </div>
</template>

<script>
import KtButton from '@/views/Core/KtButton'

export default {
  name: 'KtTable',
  components: {
    KtButton
  },
  props: {
    // 表格列配置
    columns: {
      type: Array,
      // 数组default必须是一个方法
      default: () => []
    },
    // 表格分页查询结果数据，不是分页栏
    data: {
      type: Object,
      default: null
    },
    // 编辑权限标识
    permsEdit: {
      type: String,
      default: ''
    },
    // 删除权限标识
    permsDelete: {
      type: String,
      default: ''
    },
    size: { // 尺寸样式
      type: String,
      default: 'small'
    },
    align: { // 文本对齐方式
      type: String,
      default: 'left'
    },
    maxHeight: { // 表格最大高度
      // type: Number,
      // default: 800
      type: String,
      default: 'auto'
    },
    showOperation: { // 是否显示操作组件,权限控制
      type: Boolean,
      default: true
    },
    border: { // 是否显示边框
      type: Boolean,
      default: true
    },
    stripe: { // 是否显示斑马线
      type: Boolean,
      default: true
    },
    highlightCurrentRow: { // 是否高亮当前行
      type: Boolean,
      default: true
    },
    showOverflowTooltip: { // 是否单行显示
      type: Boolean,
      default: true
    },
    showBatchDelete: { // 是否显示操作组件
      type: Boolean,
      default: true
    },
    editDisplayNone: { // 是否隐藏按钮
      type: String,
      default: null
    }
  },
  data() {
    return {
      // 分页信息
      pageRequest: {
        current: 1,
        size: 10
      },
      loading: true, // 加载标识
      selections: [] // 列表选中列
    }
  },
  // created:在模板渲染成html前调用，即通常初始化某些属性值，然后再渲染成视图。
  // mounted:在模板渲染成html后调用，通常是初始化页面完成后，再对html的dom节点进行一些需要的操作。
  mounted() {
    this.currentPageChange(1)
  },
  methods: {
    // 分页查询
    findPage: function() {
      // 不要纠结loading,瞬间刷新出数据是最好的，点一次loading一次烦死人，抢疫苗一样
      // this.loading = true
      const callback = res => {
        this.loading = false
      }
      // 自定义事件，子向父通信
      this.$emit('findPage', { pageRequest: this.pageRequest, callback: callback })
    },
    // 选择切换
    selectionChange: function(selections) {
      this.selections = selections
      this.$emit('selectionChange', { selections: selections })
    },
    // 选择切换
    handleCurrentChange: function(val) {
      this.$emit('handleCurrentChange', { val: val })
    },
    // 换页刷新
    currentPageChange: function(current) {
      this.pageRequest.current = current
      this.findPage()
    },
    // 修改页显示数量
    pageSizeChange: function(size) {
      this.pageRequest.size = size
      this.findPage()
    },
    // 编辑，生成自定义事件属性
    handleEdit: function(index, row) {
      this.$emit('handleEdit', { index: index, row: row })
    },
    // 删除
    handleDelete: function(index, row) {
      this.delete(row.id)
    },
    // 批量删除
    handleBatchDelete: function() {
      const ids = this.selections.map(item => item.id).toString()
      this.delete(ids)
    },
    // 删除操作
    delete: function(ids) {
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        // 后端接收是数组，前端也得发送是数组,得使用push方法，push选中的进数组中
        const params = []
        const idArray = (ids + '').split(',')
        for (var i = 0; i < idArray.length; i++) {
          params.push(idArray[i])
        }
        this.loading = true
        const callback = res => {
          if (res.code === 20000) {
            this.$message({ message: '删除成功', type: 'success' })
            this.findPage()
          } else {
            this.$message({ message: '操作失败, ' + res.message, type: 'error' })
          }
          this.loading = false
        }
        // $emit自定义事件
        this.$emit('handleDelete', { params: params, callback: callback })
      }).catch(() => {
      })
    }
  }
}
</script>

<style scoped>
.table{
  font-size: 14px;
}
.toolbar{
  height: 40px;
}

</style>
