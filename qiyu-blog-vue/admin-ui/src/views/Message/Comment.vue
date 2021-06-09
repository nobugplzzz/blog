<template>
  <div class="container" style="width:99%;margin-top:-15px;">
    <!--工具栏-->
    <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
      <el-form :inline="true" :model="filters" :size="size">
        <el-form-item>
          <el-input v-model="filters.label" placeholder="名称" />
        </el-form-item>
        <el-form-item>
          <KtButton icon="fa fa-edit" :label="$t('action.search')" perms="sys:comment:view" type="primary" @click="findPage(null)" />
        </el-form-item>
      </el-form>
    </div>
    <!--表格内容栏-->
    <KtTable
      perms-edit="sys:comment:edit"
      perms-delete="sys:comment:delete"
      edit-display-none="display:none;"
      :data="pageResult"
      :columns="columns"
      @findPage="findPage"
      @handleEdit="handleEdit"
      @handleDelete="handleDelete"
    />
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
      filters: {
        label: ''
      },
      columns: [
        { prop: 'id', label: 'ID', minWidth: 50 },
        { prop: 'avatar', label: '头像', minWidth: 100 },
        { prop: 'username', label: '评论人', minWidth: 100 },
        { prop: 'replayUsername', label: '回复人', minWidth: 100 },
        { prop: 'articleTitle', label: '文章标题', minWidth: 100 },
        { prop: 'content', label: '评论内容', minWidth: 100 },
        { prop: 'likeCount', label: '点赞量', minWidth: 80 },
        { prop: 'source', label: '来源', minWidth: 120 },
        { prop: 'createTime', label: '评论时间', minWidth: 120 }
        // {prop:"lastUpdateBy", label:"更新人", minWidth:100},
        // {prop:"lastUpdateTime", label:"更新时间", minWidth:120, formatter:this.dateFormat}
      ],
      pageRequest: { pageNum: 1, pageSize: 8 },
      pageResult: {}
    }
  },
  methods: {
    // 获取分页数据
    findPage: function(data) {
      if (data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.pageRequest.columnFilters = { label: { name: 'label', value: this.filters.label }}
      this.$api.comment.findPage(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data != null ? data.callback : '')
    },
    // 批量删除
    handleDelete: function(data) {
      this.$api.comment.batchDelete(data.params).then(data != null ? data.callback : '')
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
