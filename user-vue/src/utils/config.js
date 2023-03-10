// 引入axios
import axios from "axios";
// 创建axios实例
const Service = axios.create({
  // 请求的根路径
  baseURL: "http://119.91.146.102:8078/user/",
  // baseURL: "http://localhost:8078/user/",
  // 定义统一的请求头
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
  },
  header: {
    "content-Type": "application/x-www-form-urlencoded",
  },
  // 配置请求超时时间
  timeout: 5000,
});
// 请求拦截器
// Service.interceptors.request.use((config) => {
//   config.headers.common["Authorization"] =
//     window.sessionStorage.getItem("token") == null
//       ? null
//       : window.sessionStorage.getItem("token");
//   return config;
// });
// // 响应拦截器
// Service.interceptors.response.use((config) => {
//   // 获取接口返回结果
//   const res = config.data;
//   console.log("response", res);
//   if (res.status === "success") {
//     return res;
//   } else {
//     console.log("请求失败！", res.status);
//     Element.error(res.message || "网络异常");
//   }
// });
export default Service;