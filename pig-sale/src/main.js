import Vue from 'vue'
import App from './App.vue'
import './style.css'
// 导入 bootstrap 样式表
// import './assets/css/bootstrap.css'
import axios from 'axios'
// 导入api
import api from './api'
import router from './router/index'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

axios.defaults.baseURL = 'http://localhost:8080'
Vue.config.productionTip = false
Vue.prototype.$https = axios
Vue.prototype.$api = api
Vue.use(ElementUI);

new Vue({
  render: h => h(App),
  router
}).$mount('#app')


// 加载转圈
// let loadingInstance = null
// axios.interceptors.request.use(config => {
//   config.headers.Authorization = 'Ruan xxx'
//   loadingInstance = Loading.service({ fullscreen: true });
//   return config
// })
// axios.interceptors.response.use(response => {
  
//     loadingInstance.close();

//   return response
// }) 
