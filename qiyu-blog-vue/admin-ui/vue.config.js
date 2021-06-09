// vue.config.js
module.exports = {
  devServer: {
    // 跨域配置
    proxy: { //  代理所有以‘/api’开头的请求，代理到XXX, （没有匹配到静态文件的请求)
      '/api': { // path: options
        target: 'http://localhost:8081', // 目标URL
        changeOrigin: true, // 将主机标头的来源更改为目标URL,即是否跨域
        pathRewrite: { '^/api': '' } // 重写目标的url路径,即去掉'/api'
      }
    },
    disableHostCheck: true
  },
  chainWebpack: config => {
    config
    // 配置app的title，需重启
      .plugin('html')
      .tap(args => {
        args[0].title = 'My Blog'
        return args
      })
  }
}
