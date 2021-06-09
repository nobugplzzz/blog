<template>
  <div>
    <div class="title">{{ this.$route.name }}</div>
    <!-- 表格操作 -->
    <div class="operation-container">
      <el-button
        v-if="isDeleted == 0"
        style="float:left"
        type="danger"
        size="small"
        icon="el-icon-deleteItem"
        :disabled="articleIdList.length == 0"
        @click="updateIsDelete = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        type="danger"
        size="small"
        icon="el-icon-deleteItem"
        :disabled="articleIdList.length == 0"
        @click="remove = true"
      >
        批量删除
      </el-button>
      <!-- 条件筛选 -->
      <div style="margin-bottom:10px;float:right">
        <el-select
          v-model="condition"
          placeholder="请选择"
          size="small"
          style="margin-right:1rem"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入文章名"
          style="width:200px"
          @keyup.enter.native="listArticles"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listArticles(1)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <!-- 表格展示 -->
    <el-table
      v-loading="loading"
      border
      :data="articleList"
      @selection-change="selectionChange"
    >
      <!-- 表格列 -->
      <el-table-column type="selection" width="55" />
      <el-table-column prop="articleTitle" label="标题" align="center" />
      <!-- 文章分类 -->
      <el-table-column
        prop="categoryName"
        label="分类"
        width="120"
        align="center"
      />
      <!-- 文章标签 -->
      <el-table-column
        prop="tagDTOList"
        label="标签"
        width="180"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag
            v-for="item of scope.row.tagDTOList"
            :key="item.tagId"
            style="margin-right:0.2rem;margin-top:0.2rem"
          >
            {{ item.name }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 文章浏览量 -->
      <el-table-column
        prop="viewsCount"
        label="浏览量"
        width="80"
        align="center"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.viewsCount">
            {{ scope.row.viewsCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <!-- 文章点赞量 -->
      <el-table-column
        prop="likeCount"
        label="点赞量"
        width="80"
        align="center"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.likeCount">
            {{ scope.row.likeCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <!-- 文章发表时间 -->
      <el-table-column
        prop="createTime"
        label="发表时间"
        width="150"
        align="center"
        :formatter="dateFormat"
      />
      <!-- 文章修改时间 -->
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="150"
        align="center"
        :formatter="dateFormat"
      />
      <!-- 文章置顶 -->
      <el-table-column prop="isTop" label="置顶" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isTop"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.isDeleted == 1 || scope.row.isDraft == 1"
            :active-value="1"
            :inactive-value="0"
            @change="changeTop(scope.row)"
          />
        </template>
      </el-table-column>
      <!-- 列操作 -->
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.isDeleted == 0"
            type="primary"
            size="mini"
            @click="editArticle(scope.row.id)"
          >
            编辑
          </el-button>
          <el-popconfirm
            v-if="scope.row.isDeleted == 0"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateArticleLogicDelete(scope.row.id)"
          >
            <el-button slot="reference" size="mini" type="danger">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="scope.row.isDeleted == 1"
            title="确定恢复吗？"
            @confirm="updateArticleLogicDelete(scope.row.id)"
          >
            <el-button slot="reference" size="mini" type="success">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="scope.row.isDeleted == 1"
            style="margin-left:10px"
            title="确定彻底删除吗？"
            @confirm="deleteArticles(scope.row.id)"
          >
            <el-button slot="reference" size="mini" type="danger">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      class="pagination-container"
      background
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <!-- 批量逻辑删除对话框 -->
    <el-dialog :visible.sync="updateIsDelete" width="30%">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateIsDelete = false">取 消</el-button>
        <el-button type="primary" @click="updateArticleLogicDelete(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <!-- 批量彻底删除对话框 -->
    <el-dialog :visible.sync="remove" width="30%">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteArticles(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { format } from '@/utils/datetime'

export default {
  data: function() {
    return {
      loading: true,
      updateIsDelete: false,
      remove: false,
      // 筛选选项，
      options: [
        {
          value: '{"isDeleted":0,"isDraft":0}',
          label: '已发布'
        },
        {
          value: '{"isDeleted":1,"isDraft":null}',
          label: '回收站'
        },
        {
          value: '{"isDeleted":0,"isDraft":1}',
          label: '草稿箱'
        }
      ],
      // 跟options的value数据格式一致，el-select再绑定condition就等于是绑定options的某个value了
      condition: '{"isDeleted":0,"isDraft":0}',
      // 返回数据
      articleList: [],
      // 选中列表
      articleIdList: [],
      // 查询条件
      keywords: null,
      isDeleted: 0,
      isDraft: 0,
      current: 1,
      size: 10,
      count: 0
    }
  },
  watch: {
    // 观察筛选选项条件变化,只会修改变化的部分
    condition() {
      const condition = JSON.parse(this.condition)
      this.isDeleted = condition.isDeleted
      this.isDraft = condition.isDraft
      this.listArticles()
    }
  },
  created() {
    this.listArticles()
  },
  methods: {
    selectionChange(articleList) {
      this.articleIdList = []
      articleList.forEach(item => {
        this.articleIdList.push(item.id)
      })
    },
    // 跳转修改文章
    editArticle(id) {
      this.$router.push({ path: '/Article/Article/' + id })
    },
    // 逻辑删除
    updateArticleLogicDelete(id) {
      // 因为后端没有使用@RequestBody，因此拿不到请求体中的参数，需要把参数拼接到url中
      const param = new URLSearchParams()
      if (id == null) {
        param.append('idList', this.articleIdList)
      } else {
        param.append('idList', [id])
      }
      param.append('isDeleted', this.isDeleted === 0 ? 1 : 0)
      this.$api.article.updateArticleLogicDelete(param).then((res) => {
        if (res.flag) {
          this.$notify.success({
            title: '成功',
            message: res.message
          })
          this.listArticles()
        } else {
          this.$notify.error({
            title: '失败',
            message: res.message
          })
        }
        this.updateIsDelete = false
      })
    },
    // 物理删除
    deleteArticles(ids) {
      // 后端接收参数是List集合，不懂怎么传就根据swagger传参数的形式： [1，2...]
      var param = []
      if (ids == null) {
        // articleIdList是后端接收的名称，是数组，不需要一致
        param = this.articleIdList
      } else {
        // 集合添加元素：pusu
        param.push(ids)
      }
      this.$api.article.deleteArticles(param).then((res) => {
        if (res.flag) {
          this.$notify.success({
            title: '成功',
            message: res.message
          })
          this.listArticles()
        } else {
          this.$notify.error({
            title: '失败',
            message: res.message
          })
        }
        this.remove = false
      })
    },
    sizeChange(size) {
      this.size = size
      this.listArticles()
    },
    currentChange(current) {
      this.current = current
      this.listArticles()
    },
    // 置顶
    changeTop(article) {
      // URLSearchParams 接口定义了一些实用的方法来处理 URL 的查询字符串。
      const param = new URLSearchParams()
      // 插入一个指定的键/值对作为新的搜索参数。
      param.append('isTop', article.isTop)
      // restfulAPI
      this.$api.article.changeTop('/api/admin/articles/top/' + article.id, param).then((res) => {
        if (res.flag) {
          this.$notify.success({
            title: '操作成功',
            message: res.message
          })
          this.listArticles()
        } else {
          this.$notify.error({
            title: '操作失败',
            message: res.message
          })
        }
      })
    },
    // 查询文章集合
    listArticles(data) {
      // 搜索重置当前页码
      if (data === 1) {
        this.current = 1
      }
      this.$api.article.listArticles({
        current: this.current,
        size: this.size,
        keywords: this.keywords,
        isDeleted: this.isDeleted,
        isDraft: this.isDraft

      })
        .then((res) => {
          this.articleList = res.data.recordList
          this.count = res.data.count
          this.loading = false
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
