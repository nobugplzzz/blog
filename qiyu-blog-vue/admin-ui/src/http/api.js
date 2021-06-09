/*
 * 将所有接口统一起来便于维护
 * 如果项目很大可以将 url 独立成文件，接口分成不同的模块
 */
import * as login from './moudules/login'
import * as user from './moudules/user'
import * as menu from './moudules/menu'
import * as role from './moudules/role'
import * as log from './moudules/log'
import * as article from './moudules/article'
import * as category from './moudules/category'
import * as tag from './moudules/tag'
import * as message from './moudules/message'
import * as comment from './moudules/comment'

// 默认全部导出
export default {
  login,
  user,
  menu,
  role,
  log,
  article,
  category,
  tag,
  message,
  comment
}
