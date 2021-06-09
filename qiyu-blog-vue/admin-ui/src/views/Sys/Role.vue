<template>
  <div class="container" style="width:99%;margin-top:-15px;">
    <!--工具栏-->
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-input v-model="pageRequest.keywords" placeholder="角色名" />
        </el-form-item>
        <el-form-item>
          <KtButton :label="$t('action.search')" perms="sys:role:view" type="primary" @click="listRoles(null)" />
        </el-form-item>
        <el-form-item>
          <KtButton :label="$t('action.add')" perms="sys:role:add" type="primary" @click="handleAdd" />
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <KtTable
      perms-edit="sys:role:edit"
      perms-delete="sys:role:delete"
      :data="pageResult"
      :columns="columns"
      :stripe="false"
      :highlight-current-row="true"
      :show-batch-delete="true"
      @findPage="listRoles"
      @handleEdit="handleEdit"
      @handleDelete="handleDelete"
    />
    <!--新增编辑界面-->
    <el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :model="dataForm" label-width="80px" :rules="dataFormRules" :size="size">
        <el-form-item v-if="false" label="ID" prop="id">
          <el-input v-model="dataForm.id" :disabled="true" auto-complete="off" />
        </el-form-item>
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="dataForm.roleName" auto-complete="off" />
        </el-form-item>
        <el-form-item label="标签" prop="roleLabel">
          <el-input v-model="dataForm.roleLabel" auto-complete="off" />
        </el-form-item>
        <el-form-item label="备注 " prop="roleRemark">
          <el-input v-model="dataForm.roleRemark" auto-complete="off" type="textarea" />
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
// import TableTreeColumn from '@/views/Core/TableTreeColumn'
import { format } from '@/utils/datetime'

export default {
  components: {
    KtTable,
    KtButton
    // TableTreeColumn
  },
  data() {
    return {
      size: 'small',
      columns: [
        { prop: 'id', label: 'ID', minWidth: 50 },
        { prop: 'roleName', label: '角色名', minWidth: 120 },
        { prop: 'roleLabel', label: '标签', minWidth: 120 },
        { prop: 'roleRemark', label: '备注', minWidth: 120 },
        { prop: 'isDisable', label: '是否禁用', minWidth: 120 },
        { prop: 'createTime', label: '创建时间', minWidth: 120, formatter: this.dateFormat }
        // {prop:"lastUpdateBy", label:"更新人", minWidth:100},
        // {prop:"lastUpdateTime", label:"更新时间", minWidth:120, formatter:this.dateFormat}
      ],
      // 请求参数
      pageRequest: { pageNum: 1, pageSize: 8, keywords: '' },
      pageResult: {},

      operation: false, // true:新增, false:编辑
      editDialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        roleName: [
          { required: true, message: '请输入角色名', trigger: 'blur' }
        ],
        roleLabel: [
          { required: true, message: '请输入角色标签', trigger: 'blur' }
        ],
        roleRemark: [
          { required: true, message: '请输入角备注', trigger: 'blur' }
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id: 0,
        roleName: '',
        roleLabel: '',
        roleRemark: ''
      },
      // 角色菜单数据
      selectRole: {},
      menuData: [],
      menuSelections: [],
      menuLoading: false,
      authLoading: false,
      checkAll: false,
      currentRoleMenus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  mounted() {
  },
  methods: {
    // 获取分页数据
    listRoles: function(data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.$api.role.listRoles(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function(data) {
      this.$api.role.deleteRoles(data.params).then(data.callback)
    },
    // 显示新增界面
    handleAdd: function() {
      this.editDialogVisible = true
      this.operation = true
      this.dataForm = {
        id: 0,
        name: '',
        remark: ''
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
            this.$api.role.saveOrUpdateRole(params).then((res) => {
              this.editLoading = false
              if (res.code === 20000) {
                this.$message({ message: '操作成功', type: 'success' })
              } else {
                this.$message({ message: '操作失败, ' + res.message, type: 'error' })
              }
              this.$refs['dataForm'].resetFields()
              this.editDialogVisible = false
              this.listRoles(null)
            })
          })
        }
      })
    },
    // 树节点选择监听
    handleMenuCheckChange(data, check, subCheck) {
      if (check) {
        // 节点选中时同步选中父节点
        const parentId = data.parentId
        this.$refs.menuTree.setChecked(parentId, true, false)
      } else {
        // 节点取消选中时同步取消选中子节点
        if (data.children != null) {
          data.children.forEach(element => {
            this.$refs.menuTree.setChecked(element.id, false, false)
          })
        }
      }
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
