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
        <el-form-item v-if="false" label="ID" prop="userInfoId">
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
            @change="changeDisable()"
          />
        </el-form-item>
        <!-- 设置角色栏 -->
        <el-form-item label="角色" align="left">
          <el-checkbox-group v-model="roleIdList">
            <el-checkbox
              v-for="item of userRoleList"
              :key="item.id"
              :label="item.id"
            >
              {{ item.roleName }}
            </el-checkbox>
          </el-checkbox-group>
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
        { prop: 'userInfoId', label: 'ID', minWidth: 50 },
        { prop: 'avatar', label: '头像', minWidth: 120 },
        { prop: 'nickname', label: '昵称', minWidth: 100 },
        { prop: 'roleList', label: '用户角色', minWidth: 120, formatter: this.roleListFormat },
        { prop: 'isDisabled', label: '禁用', minWidth: 100 },
        { prop: 'ipAddr', label: 'ip地址', minWidth: 70 },
        { prop: 'ipSource', label: 'ip来源', minWidth: 120 },
        { prop: 'createTime', label: '创建时间', minWidth: 120, formatter: this.dateFormat },
        { prop: 'lastLoginTime', label: '上次登录时间', minWidth: 100 }
      ],
      pageRequest: { pageNum: 1, pageSize: 8, keywords: '' },
      pageResult: {},
      // 角色选项列表
      userRoleList: [],
      // 需要roleId和多选框绑定
      roleIdList: [],
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
        userInfoId: 0,
        nickname: '',
        isDisabled: ''
      }
    }
  },
  // 钩子
  mounted() {
  },
  methods: {
    // 查询用户列表和角色列表
    listUsers: function(data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.$api.user.listUsers(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data != null ? data.callback : '')
      this.$api.role.listUserRoles().then((res) => {
        this.userRoleList = res.data
      })
    },
    // 显示编辑界面
    handleEdit: function(params) {
      this.editDialogVisible = true
      this.operation = false
      // Object.assign(target, ...sources) 方法用于将所有可枚举属性的值从一个或多个源对象分配到目标对象。它将返回目标对象。assign：分配
      // params.row的属性被分配到{}里，返回带属性的对象,row是js的表格行属性,也是KtTable的handleEdit方法参数
      this.dataForm = Object.assign({}, params.row)
      this.roleIdList = []
      params.row.roleList.forEach((item) => {
        this.roleIdList.push(item.id)
      })
    },
    // 编辑表单提交
    editSubmit: function() {
      // 表单检查
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            // 复制属性，从源对象复制到目标，返回目标对象
            const params = Object.assign({}, this.dataForm)
            params.roleIdList = this.roleIdList
            this.$api.user.updateUserRole(params).then((res) => {
              if (res.code === 20000) {
                this.editDialogVisible = false
                this.$message({ message: '操作成功', type: 'success' })
                this.$refs['dataForm'].resetFields()
              } else {
                this.$message({ message: '操作失败, ' + res.message, type: 'error' })
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
    },
    // 禁用开关状态,options是changeDisable方法传过来的参数的引用
    changeDisable() {
      // URLSearchParams 接口定义了一些实用的方法来处理 URL 的查询字符串。参数连接在url后面
      const param = new URLSearchParams()
      // 插入一个指定的键/值对作为新的搜索参数。
      param.append('isDisabled', this.dataForm.isDisabled)
      this.$api.user.updateUserSilence('/api/admin/users/disable/' + this.dataForm.userInfoId, param).then((res) => {
        if (res.code === 20000) {
          this.$message({ message: '操作成功', type: 'success' })
        } else {
          this.$message({ message: '操作失败, ' + res.message, type: 'error' })
        }
      })
    },
    roleListFormat: function(row, column, cellValue, index) {
      const arr = []

      row.roleList.forEach((item, index) => {
        // console.log(item.roleName + ',' + index)
        arr.push(item.roleName)
      })
      return arr.join('\n')
    }

  }
}

</script>

<style >
.el-table .cell {
  white-space: pre-line !important;
}
</style>

