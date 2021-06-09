<template>
  <div class="container" style="width:99%;margin-top:-15px;">
    <!--工具栏-->
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-input v-model="pageRequest.keywords" placeholder="姓名" />
        </el-form-item>
        <el-form-item>
          <!-- <el-button type="primary" :size="size" @click="findPage(null)">查询</el-button> -->
          <KtButton :label="$t('action.search')" perms="sys:role:view" type="primary" @click="listUsers(null)" />
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <KtTable
      perms-edit="sys:user:edit"
      perms-delete="sys:user:delete"
      :show-batch-delete="false"
      :data="pageResult"
      :columns="columns"
      @findPage="listUsers"
      @handleEdit="handleEdit"
    />
    <!--新增或编辑对话框界面-->
    <el-dialog title="编辑" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" :rules="dataFormRules" :size="size" label-position="right">
        <el-form-item v-if="false" label="ID" prop="id">
          <el-input v-model="dataForm.id" :disabled="true" auto-complete="off" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="dataForm.nickname" auto-complete="off" />
        </el-form-item>
        <!-- 设置禁用 -->
        <el-form-item label="禁用" align="left">
          <el-switch
            v-model="dataForm.isDisabled"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
        <!-- 设置角色栏 -->
        <el-form-item label="角色" align="left">
          <el-radio-group v-model="dataForm.roleId">
            <el-radio :label="1">管理员</el-radio>
            <el-radio :label="2">用户</el-radio>
            <el-radio :label="3">测试</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="editDialogVisible = false">取消</el-button>
        <el-button :size="size" type="primary" :loading="editLoading" @click.native="editSubmit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import KtButton from '@/views/Core/KtButton'
import KtTable from '@/views/Core/KtTable'
import { format } from '@/utils/datetime'

export default {
  components: {
    KtButton,
    KtTable
  },
  data() {
    return {
      size: 'small',
      // 表格数据
      columns: [
        { prop: 'id', label: 'ID', minWidth: 50 },
        { prop: 'avatar', label: '头像', minWidth: 120 },
        { prop: 'nickname', label: '昵称', minWidth: 100 },
        { prop: 'roleId', label: '用户角色', minWidth: 120 },
        { prop: 'isDisabled', label: '禁用', minWidth: 100 },
        { prop: 'ipAddr', label: 'ip地址', minWidth: 70 },
        { prop: 'ipSource', label: 'ip来源', minWidth: 120 },
        { prop: 'createTime', label: '创建时间', minWidth: 120, formatter: this.dateFormat },
        { prop: 'lastLoginTime', label: '上次登录时间', minWidth: 100 }
      ],
      pageRequest: { pageNum: 1, pageSize: 8, keywords: '' },
      pageResult: {},

      // 编辑对话框
      editDialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        nickname: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
      // 编辑界面数据
      dataForm: {
        id: 0,
        nickname: '',
        isDisabled: '',
        roleId: ''
      }
    }
  },
  // 钩子
  mounted() {
  },
  methods: {
    // 获取分页数据
    listUsers: function(data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.$api.user.listUsers(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data != null ? data.callback : '')
    },
    // 显示编辑界面
    handleEdit: function(params) {
      this.editDialogVisible = true
      this.operation = false
      // Object.assign(target, ...sources) 方法用于将所有可枚举属性的值从一个或多个源对象分配到目标对象。它将返回目标对象。assign：分配
      // params.row的属性被分配到{}里，返回带属性的对象,row是js的表格行属性,也是KtTable的handleEdit方法参数
      this.dataForm = Object.assign({}, params.row)
    },
    // 编辑表单提交
    editSubmit: function() {
      // 表单检查
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            // 复制属性，从源对象复制到目标，返回目标对象
            const params = Object.assign({}, this.dataForm)
            this.$api.user.changeUserRole(params).then((res) => {
              this.editLoading = false
              if (res.code === 20000) {
                this.$message({ message: '操作成功', type: 'success' })
                this.editDialogVisible = false
                this.$refs['dataForm'].resetFields()
              } else {
                this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
              }
              // id引用，ref引用的信息都会注册在父组件$refs对象上，可以聚焦指定组件
              // this.$refs['dataForm'].resetFields()
              this.listUsers(null)
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

