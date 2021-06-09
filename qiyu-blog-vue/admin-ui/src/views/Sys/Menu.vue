<template>
  <div class="container" style="width:99%;margin-top:-15px;">
    <!--工具栏-->
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :size="size">
        <el-form-item>
          <el-input v-model="keywords" placeholder="名称" />
        </el-form-item>
        <el-form-item>
          <kt-button :label="$t('action.search')" perms="sys:menu:view" type="primary" @click="listMenus(keywords)" />
        </el-form-item>
        <el-form-item>
          <kt-button :label="$t('action.add')" perms="sys:menu:add" type="primary" @click="handleAdd" />
        </el-form-item>
        <el-form-item>
          <el-button
            type="danger"
            size="medium"
            icon="el-icon-deleteItem"
            :disabled="selections.length == 0"
            @click="handleDelete(selections)"
          >
            批量删除
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表格树内容栏-->
    <el-table
      :data="tableTreeData"
      stripe
      size="mini"
      style="width: 100%;"
      element-loading-text="拼命加载中"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        width="80"
        label="ID"
      />
      <TableTreeColumn
        prop="name"
        header-align="center"
        tree-key="id"
        width="150"
        label="名称"
      />
      <el-table-column header-align="center" align="center" label="图标">
        <template slot-scope="scope">
          <i :class="scope.row.icon || ''" />
        </template>
      </el-table-column>
      <el-table-column prop="type" header-align="center" align="center" label="类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" size="small">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 1" size="small" type="success">菜单</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        width="160"
        label="组件路径"
      >
        <template v-slot="scope">
          <span v-if="scope.row.parentId!==0">
            {{ scope.row.url +'.vue' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="url"
        header-align="center"
        align="center"
        width="150"
        :show-overflow-tooltip="true"
        label="菜单URL"
      />
      <el-table-column
        prop="isDisable"
        header-align="center"
        align="center"
        width="100"
        :show-overflow-tooltip="true"
        label="禁用"
      >
        <template slot-scope="scope">
          <!-- <el-tooltip :content="'isDisable value: ' + scope.row.isDisable" placement="top"> -->
          <el-switch
            v-if="scope.row.parentId !== 0 "
            v-model="scope.row.isDisable"
            active-color="#13ce66"
            inactive-color="rgb(184 184 185)"
            :active-value="1"
            :inactive-value="0"
            @change="changeDisable(scope.row)"
          />
          <!-- </el-tooltip> -->
        </template>
      </el-table-column>
      <el-table-column
        prop="isHidden"
        header-align="center"
        align="center"
        width="100"
        :show-overflow-tooltip="true"
        label="隐藏"
      >
        <template slot-scope="scope">
          <!-- <el-tooltip :content="'isHidden value: ' + scope.row.isHidden" placement="top"> -->
          <el-switch
            v-if="scope.row.parentId !== 0 "
            v-model="scope.row.isHidden"
            active-color="#13ce66"
            inactive-color="rgb(184 184 185)"
            :active-value="1"
            :inactive-value="0"
            @change="changeDisable(scope.row)"
          />
          <!-- </el-tooltip> -->
        </template>
      </el-table-column>
      <el-table-column
        prop="orderNum"
        header-align="center"
        align="center"
        label="排序"
      />
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="180"
        label="操作"
      >
        <template slot-scope="scope">
          <kt-button :label="$t('action.edit')" perms="sys:menu:edit" @click="handleEdit(scope.row)" />
          <kt-button :label="$t('action.delete')" perms="sys:menu:delete" type="danger" @click="handleDelete(scope.row)" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增修改界面 -->
    <el-dialog :title="!dataForm.id ? '新增' : '编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false">
      <el-form
        ref="dataForm"
        :model="dataForm"
        :rules="dataRule"
        label-width="80px"
        :size="size"
        style="text-align:left;"
        @keyup.enter.native="dataFormSubmit()"
      >
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="dataForm.type">
            <el-radio v-for="(type, index) in menuTypeList" :key="index" :label="index">{{ type }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="menuTypeList[dataForm.type] + '名称'" prop="name">
          <el-input v-model="dataForm.name" :placeholder="menuTypeList[dataForm.type] + '名称'" />
        </el-form-item>
        <el-form-item v-if="menuTypeList[dataForm.type] ==='菜单'" label="上级菜单" prop="parentName">
          <!-- 弹出菜单树 -->
          <PopupTreeInput
            :data="data"
            :props="popupTreeProps"
            :prop="dataForm.parentName==null||dataForm.parentName==''?'请选择菜单':dataForm.parentName"
            :node-key="''+dataForm.parentId"
            :current-change-handle="handleTreeSelectChange"
          />
        </el-form-item>
        <el-form-item v-if="dataForm.type === 1" label="菜单路由" prop="url">
          <el-row>
            <el-col :span="22">
              <el-input v-model="dataForm.url" placeholder="菜单路由" />
            </el-col>
            <el-col :span="2" class="icon-list__tips">
              <el-tooltip placement="top" effect="light" style="padding: 10px;">
                <div slot="content">
                  <p>URL格式：</p>
                  <p>1.常规业务开发的功能URL，如用户管理，Views目录下页面路径为 /Sys/User, 此处填写 /sys/user。</p>
                  <p>2.嵌套外部网页，如通过菜单打开百度网页，此处填写 http://www.baidu.com，http:// 不可省略。</p>
                  <p>示例：用户管理：/sys/user 嵌套百度：http://www.baidu.com 嵌套网页：http://127.0.0.1:8000</p></div>
                <i class="el-icon-warning" />
              </el-tooltip>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item v-if="dataForm.type !== 2" label="排序编号" prop="orderNum">
          <el-input-number v-model="dataForm.orderNum" controls-position="right" :min="0" label="排序编号" />
        </el-form-item>
        <el-form-item v-if="dataForm.type !== 2" label="菜单图标" prop="icon">
          <el-row>
            <el-col :span="22">
              <el-input v-model="dataForm.icon" v-popover:iconListPopover :readonly="false" placeholder="菜单图标名称（如：fa fa-home fa-lg）" class="icon-list__input" />
            </el-col>
            <el-col :span="2" class="icon-list__tips">
              <!-- 图标提示 -->
              <FaIconTooltip />
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button :size="size" @click="dialogVisible = false">取消</el-button>
        <el-button :size="size" type="primary" @click="dataFormSubmit()">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import KtButton from '@/views/Core/KtButton'
import TableTreeColumn from '@/views/Core/TableTreeColumn'
import PopupTreeInput from '@/components/PopupTreeInput'
import FaIconTooltip from '@/components/FaIconTooltip'

export default {
  components: {
    PopupTreeInput,
    KtButton,
    TableTreeColumn,
    FaIconTooltip
  },
  data() {
    return {
      size: 'small',
      loading: false,
      // 表格菜单展示数据
      tableTreeData: [],
      // 新增/编辑弹出菜单展示数据
      data: Object,
      dialogVisible: false,
      // 菜单类型
      menuTypeList: ['目录', '菜单'],
      // 新增/编辑行数据
      dataForm: {
        id: 0,
        type: 1,
        name: '',
        parentId: 0,
        url: '',
        orderNum: 0,
        icon: ''
      },
      dataRule: {
        name: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ]
      },
      // 弹出树形菜单的配置，展示数据要有这两个属性
      popupTreeProps: {
        label: 'name',
        children: 'children'
      },
      // 搜索词
      keywords: '',
      // 列表选中列
      selections: []
    }
  },
  // 加载vue组件就会加载钩子函数
  mounted() {
    this.listMenus()
  },
  methods: {
    // 获取菜单数据
    listMenus: function() {
      this.loading = true
      this.$api.menu.listMenus({ keywords: this.keywords }).then((res) => {
        this.tableTreeData = res.data
        // 新增/编辑界面的弹出菜单数据
        this.data = this.tableTreeData
        this.loading = false
      })
    },
    // 显示新增界面
    handleAdd: function() {
      this.dialogVisible = true
      this.dataForm = {
        id: 0,
        type: 1,
        name: '',
        parentId: 0,
        url: '',
        orderNum: 0,
        icon: ''
      }
    },
    // 显示编辑界面
    handleEdit: function(row) {
      this.dialogVisible = true
      //  Object.assign 类似 org.springframework.beans.BeanUtils.copyProperties(Object source, Object target)
      Object.assign(this.dataForm, row)
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        var params
        if (row.id == null) {
          params = row
        } else {
          params = this.getDeleteIds([], row)
        }
        this.$api.menu.deleteMenus(params).then(res => {
          this.listMenus()
          if (res.code === 20000) {
            this.$message({ message: '操作成功', type: 'success' })
          } else {
            this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
          }
        })
      })
    },

    // 获取删除的包含子菜单的id列表
    getDeleteIds(ids, row) {
      // ids.push({ id: row.id })
      ids.push(row.id)
      if (row.children != null) {
        for (let i = 0, len = row.children.length; i < len; i++) {
          this.getDeleteIds(ids, row.children[i])
        }
      }
      return ids
    },
    // 菜单树选中
    handleTreeSelectChange(data, node) {
      this.dataForm.parentId = data.id
      this.dataForm.parentName = data.name
    },

    // 图标选中
    iconActiveHandle(iconName) {
      this.dataForm.icon = iconName
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        this.editLoading = true
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = false
            const params = Object.assign({}, this.dataForm)
            this.$api.menu.saveOrUpdateMenu(params).then((res) => {
              if (res.code === 20000) {
                this.$message({ message: '操作成功', type: 'success' })
              } else {
                this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
              }
              this.$refs['dataForm'].resetFields()
              this.dialogVisible = false
              this.listMenus()
            })
          })
        }
      })
    },
    // 禁用/隐藏开关状态,options是changeDisable方法传过来的参数的引用
    changeDisable(options) {
      // URLSearchParams 接口定义了一些实用的方法来处理 URL 的查询字符串。
      const param = new URLSearchParams()
      // 插入一个指定的键/值对作为新的搜索参数。
      param.append('isDisable', options.isDisable)
      param.append('isHidden', options.isHidden)
      this.$api.menu.updateMenuOptions('/api/admin/menu/options/' + options.id, param).then((res) => {
        if (res.code === 20000) {
          this.$message({ message: '操作成功', type: 'success' })
        } else {
          this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
        }
      })
      if (options.parentId === 0) {
        alert(options.parentId)
      }
    },
    // 批量选择row切换为批量选择row.id，参数selections选择事件自带，打勾默认选择整个row
    selectionChange(selections) {
      this.selections = []
      selections.forEach(item => {
        this.selections.push(item.id)
      })
    }
  }
}
</script>

<style scoped>

</style>
