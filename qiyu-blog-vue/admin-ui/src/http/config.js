
import { baseUrl } from '@/utils/global'

// axios基础配置，axios请求选择带有如下信息，这里都是可选信息，在axios实例选择或者拦截器配置
export default {
  method: 'get',
  // 基础url前缀
  baseUrl: baseUrl,
  // 请求头信息
  hearders: {
    'Content-Type': 'application/json;charset=UTF-8'
  },
  // 任何方法都带params,这里不需要写出来，使用params是拼接参数到URL，无论get还是post都是拼接params，axios实例直接用
  // params: {},

  // post方法参数，参数在请求体中
  data: {},
  // 设置超时时间
  timeout: 10000,
  // 携带凭证，当配置了xhr.withCredentials = true时，必须在后端增加 response 头信息Access-Control-Allow-Origin，且必须指定域名，而不能指定为*。
  withCredentials: true,
  // 返回数据类型
  responseType: 'json'
}
