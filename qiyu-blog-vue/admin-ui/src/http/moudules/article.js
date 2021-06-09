import axios from '../axios'

/*
 * 文章管理模块
 */

// 保存

// 分页查询
export const listArticles = (data) => {
  return axios({
    url: '/api/admin/articles',
    method: 'get',
    params: data
  })
}

// 修改置顶
export function changeTop(url, data) {
  return axios({
    url: url,
    method: 'put',
    data
  })
}

// 根据id查询文章
export function getArticleById(url) {
  return axios({
    url: url,
    method: 'get'
  })
}

// 查询新建/编辑文章的分类和标签选项
export function listArticleOptionDTO() {
  return axios({
    url: '/api/admin/articles/options',
    method: 'get'
  })
}

// 新建或修改文章
export function saveOrUpdateArticle(data) {
  return axios({
    url: '/api/admin/articles',
    method: 'post',
    data
  })
}

// 物理删除文章
export function deleteArticles(data) {
  return axios({
    url: '/api/admin/articles',
    method: 'delete',
    data
  })
}

// 逻辑删除文章
export function updateArticleLogicDelete(data) {
  return axios({
    url: '/api/admin/articles',
    method: 'put',
    data
  })
}
