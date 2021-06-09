<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <!-- 文章标题 -->
    <div class="article-title-container">
      <el-input
        v-model="article.articleTitle"
        size="medium"
        placeholder="输入文章标题"
      />
      <el-button
        v-if="article.isDraft != 0"
        type="danger"
        size="medium"
        class="save-btn"
        @click="saveArticleDraft"
      >
        保存草稿
      </el-button>
      <el-button
        type="danger"
        size="medium"
        style="margin-left:10px"
        @click="addOrEdit = true"
      >
        发布文章
      </el-button>
    </div>
    <!-- 文章内容 -->
    <mavon-editor
      ref="md"
      v-model="article.articleContent"
      style="height:calc(100vh - 260px);margin-top:10px;"
      @imgAdd="uploadImg"
    />
    <!-- 添加文章对话框 -->
    <el-dialog :visible.sync="addOrEdit" width="40%" top="10vh">
      <div slot="title" class="dialog-title-container">
        上传文章
      </div>
      <!-- 文章数据 -->
      <el-form class="el-form" label-width="80px" size="medium" :model="article">
        <el-form-item label="文章分类">
          <el-select v-model="article.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.categoryName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文章标签">
          <el-select
            v-model="article.tagIdList"
            multiple
            placeholder="请选择标签"
          >
            <el-option
              v-for="item in tagList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上传封面">
          <el-upload
            class="upload-cover"
            drag
            action="/api/admin/articles/images"
            multiple
            :on-success="uploadCover"
          >
            <i v-if="article.articleCover === ''" class="el-icon-upload" />
            <div v-if="article.articleCover === ''" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img
              v-else
              :src="article.articleCover"
              width="360px"
              height="180px"
            >
          </el-upload>
        </el-form-item>
        <el-form-item label="置顶">
          <el-switch
            v-model="article.isTop"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateArticle">
          发 表
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data: function() {
    return {
      addOrEdit: false,
      autoSave: true,
      // 分类列表
      categoryList: [],
      // 标签列表
      tagList: [],
      // 文章
      article: {
        id: null,
        articleTitle: '',
        articleContent: '',
        articleCover: '',
        categoryId: null,
        tagIdList: [],
        isTop: 0,
        isDraft: null
      }
    }
  },
  created() {
    // 获取文章
    const path = this.$route.path
    const arr = path.split('/')
    const articleId = arr[3]
    if (articleId) {
      this.$api.article.getArticleById('/api/admin/articles/' + articleId).then((res) => {
        this.article = res.data
      })
    }
    this.listArticleOptions()
  },
  destroyed() {
    // 文章自动保存功能
    this.autoSaveArticle()
  },
  methods: {
    // 查询 查询新建/编辑文章的分类和标签选项
    listArticleOptions() {
      this.$api.article.listArticleOptionDTO().then((res) => {
        this.categoryList = res.data.categoryBackDTOList
        this.tagList = res.data.tagDTOList
      })
    },
    uploadCover(response) {
      this.article.articleCover = response.data
    },
    uploadImg(pos, file) {
      var formdata = new FormData()
      formdata.append('file', file)
      this.$api.article
        .post('/api/admin/articles/images', formdata)
        .then(({ data }) => {
          this.$refs.md.$img2Url(pos, data.data)
        })
    },
    saveArticleDraft() {
      if (this.article.articleTitle.trim() === '') {
        this.$message.error('文章标题不能为空')
        return false
      }
      if (this.article.articleContent.trim() === '') {
        this.$message.error('文章内容不能为空')
        return false
      }
      this.article.isDraft = 1
      this.$api.article.post('/api/admin/articles', this.article).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: '保存草稿成功'
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: '保存草稿失败'
          })
        }
      })
      // 关闭自动保存功能
      this.autoSave = false
    },
    saveOrUpdateArticle() {
      if (this.article.articleTitle.trim() === '') {
        this.$message.error('文章标题不能为空')
        return false
      }
      if (this.article.articleContent.trim() === '') {
        this.$message.error('文章内容不能为空')
        return false
      }
      if (!this.article.categoryId) {
        this.$message.error('文章分类不能为空')
        return false
      }
      if (this.article.tagIdList.length === 0) {
        this.$message.error('文章标签不能为空')
        return false
      }
      // if (this.article.articleCover.trim() === '') {
      //   this.$message.error('文章封面不能为空')
      //   return false
      // }
      this.article.isDraft = 0
      this.$api.article.saveOrUpdateArticle(this.article).then((res) => {
        if (res.flag) {
          this.$notify.success({
            title: '成功',
            message: res.message
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: res.message
          })
        }
        this.addOrEdit = false
      })
      // 关闭自动保存功能
      this.autoSave = false
    },
    autoSaveArticle() {
      if (
        this.autoSave &&
        this.article.articleTitle.trim() !== '' &&
        this.article.articleContent.trim() !== ''
      ) {
        this.article.isDraft =
          this.article.isDraft === 0 ? this.article.isDraft : 1
        this.$api.article
          .post('/api/admin/articles', this.article)
          .then(({ data }) => {
            if (data.flag) {
              this.$notify.success({
                title: '成功',
                message: '自动保存成功'
              })
            } else {
              this.$notify.error({
                title: '失败',
                message: '自动保存失败'
              })
            }
          })
      }
    }
  }
}
</script>

<style scoped>
.main-card{
 width: 100%;
box-shadow: 0 0 0 0 rgba(0,0,0,.1);
border-color: white;
 }
 .title{
     font-size: 20px;
 }
.article-title-container {
  display: flex;
  align-items: center;
}
.save-btn {
  background: #fff;
  color: #f56c6c;
}
.el-form{
    text-align: left;
}
</style>
