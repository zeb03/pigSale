import axios from "axios";
import Element from "element-ui";
import router from "@/router";

let request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000
})

// 添加响应拦截器
request.interceptors.response.use(function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    let res = response.data
    if (res.code === 200) {
        console.log(200)
        return res

    } else {
        //重定向 ||权限不足
        if (res.code === 301 || res.code === 401) {
            Element.Message.error(res.msg)
            //跳转到登录页面
            router.push({path: "/login"})
            return Promise.reject(res.msg)
        }
        Element.Message.error(!res.msg ? "系统异常，请联系管理员" : res.msg)
        return Promise.reject(res.msg)

    }
}, function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    if (error.response.status === 404) {
        Element.Message.error("404")
    }
    return Promise.reject(error);
});

export default request