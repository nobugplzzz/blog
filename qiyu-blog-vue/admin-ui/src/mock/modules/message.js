/*
 * 字典管理模块
 */

// 保存
export function save() {
  return {
    url: 'message/save',
    type: 'post',
    data: {
      'code': 200,
      'msg': null,
      'data': 1
    }
  }
}
// 批量删除
export function batchDelete() {
  return {
    url: 'message/delete',
    type: 'post',
    data: {
      'code': 200,
      'msg': null,
      'data': 1
    }
  }
}
// 分页查询
export function findPage(params) {
  const findPageData = {
    'code': 200,
    'msg': null,
    'data': {}
  }
  const pageNum = 1
  const pageSize = 8
  if (params !== null) {
    // pageNum = params.pageNum
  }
  if (params !== null) {
    // pageSize = params.pageSize
  }
  const content = this.getContent(pageNum, pageSize)
  findPageData.data.pageNum = pageNum
  findPageData.data.pageSize = pageSize
  findPageData.data.totalSize = 50
  findPageData.data.content = content
  return {
    url: 'message/findPage',
    type: 'post',
    data: findPageData
  }
}
export function getContent(pageNum, pageSize) {
  const content = []
  for (let i = 0; i < pageSize; i++) {
    const obj = {}
    const index = ((pageNum - 1) * pageSize) + i + 1
    obj.id = index
    obj.avatar = 'avatar' + index
    obj.username = 'username' + index
    obj.content = 'content' + index
    obj.description = 'description' + index
    obj.sort = 0
    obj.ipAddress = 'ipAddress' + index
    obj.ipSource = 'ipSource' + index
    if (i % 2 === 0) {
      // 任意名称
    }
    obj.createBy = 'admin'
    obj.createTime = '2018-08-14 11:11:11'
    content.push(obj)
  }
  return content
}
