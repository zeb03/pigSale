import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from '../views/LoginView.vue'
import AdminHomeView from '../views/AdminHomeView.vue'
import MyGoods from '../components/menus/MyGoods.vue'
import MyCategory from '../components/menus/MyCategory.vue'
import MyOrders from '../components/menus/MyOrders.vue'
import MyAdmin from '../components/menus/MyAdmin.vue'
import MyUsers from '../components/menus/MyUsers.vue'
import MyPermission from '../components/menus/MyPermission.vue'


Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginView },
    {
        path: '/adminHome',
        redirect: '/adminHome/goods',
        component: AdminHomeView,
        children: [
            { path: 'users', component: MyUsers },
            { path: 'goods', component: MyGoods },
            { path: 'orders', component: MyOrders },
            { path: 'category', component: MyCategory },
            { path: 'admin', component: MyAdmin },
            { path: 'users', component: MyUsers },
            { path: 'permission', component: MyPermission },
        ]
    },
]

const router = new VueRouter({
    routes
})

// 拦截器
router.beforeEach((to, from, next) => {
    if (to.path === '/login') {
        return next()
    }
    const token = localStorage.getItem('token')
    if (!token) {
        next('/login')
    }
    next()
})

export default router






