<template>
  <div class="container" style="width:99%;margin-top:-15px;">
    <!--工具栏-->
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-input v-model="pageRequest.keywords" placeholder="名称" />
        </el-form-item>
        <el-form-item>
          <KtButton icon="fa fa-search" :label="$t('action.search')" perms="sys:tag:view" type="primary" @click="listTags(null)" />
        </el-form-item>
        <el-form-item>
          <KtButton icon="fa fa-plus" :label="$t('action.add')" perms="sys:tag:add" type="primary" @click="handleAdd" />
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <KtTable
      perms-edit="sys:tag:edit"
      perms-delete="sys:tag:delete"
      :data="pageResult"
      :columns="columns"
      @findPage="listTags"
      @handleEdit="handleEdit"
      @handleDelete="handleDelete"
    />
    <!--新增/编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" :rules="dataFormRules" :size="size">
        <el-form-item v-if="false" label="ID" prop="id">
          <el-input v-model="dataForm.id" :disabled="true" auto-complete="off" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" auto-complete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="editDialogVisible = false">取消</el-button>
        <el-button :size="size" type="primary" :loading="editLoading" @click.native="submitForm">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import KtTable from '@/views/Core/KtTable'
import KtButton from '@/views/Core/KtButton'
import { format } from '@/utils/datetime'

export default {
  components: {
    KtTable,
    KtButton
  },
  data() {
    return {
      size: 'small',
      columns: [
        // { prop: 'id', label: 'ID', minWidth: 50 },
        { prop: 'name', label: '名称', minWidth: 100 },
        { prop: 'createTime', label: '创建时间', minWidth: 120, formatter: this.dateFormat },
        { prop: 'updateTime', label: '修改时间', minWidth: 120, formatter: this.dateFormat }
        // {prop:"lastUpdateBy", label:"更新人", minWidth:100},
        // {prop:"lastUpdateTime", label:"更新时间", minWidth:120, formatter:this.dateFormat}
      ],
      pageRequest: { pageNum: 1, pageSize: 2, keywords: '' },
      pageResult: {},

      operation: false, // true:新增, false:编辑
      editDialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: null, // =========================he keywords
        name: ''
      }
    }
  },
  methods: {
    // 获取分页数据，data是子组件KtTable传过来的，参数是pageRequest、callback，也就是说KtTable的pageRequest优先级高于父组件
    listTags: function(data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      } else {
        // 查询结果第一页显示
        this.pageRequest.current = 1
      }
      this.$api.tag.listTags(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function(data) {
      this.$api.tag.deleteTag(data.params).then(data != null ? data.callback : '')
    },
    // 显示新增界面
    handleAdd: function() {
      this.editDialogVisible = true
      this.operation = true
      this.dataForm = {
        id: null,
        name: ''
      }
    },
    // 显示编辑界面
    handleEdit: function(params) {
      this.editDialogVisible = true
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
    },
    // 编辑
    submitForm: function() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            const params = Object.assign({}, this.dataForm)
            this.$api.tag.saveOrUpdateTag(params).then((res) => {
              if (res.code === 20000) {
                this.$message({ message: '操作成功', type: 'success' })
              } else {
                this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
              }
              this.editLoading = false
              this.$refs['dataForm'].resetFields()
              this.editDialogVisible = false
              this.listTags(null)
            })
          })
        }
      })
    },
    // 时间格式化
    dateFormat: function(row, column, cellValue, index) {
      return format(row[column.property])
    }
  }
}
</script>

<style scoped>

</style>
