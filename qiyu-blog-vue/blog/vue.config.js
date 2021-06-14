module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    port: 80,     // 端口号
    proxy: {
      "/api": {
        target: "http://localhost:8081",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        }
      }
    },
    disableHostCheck: true
  }
};
