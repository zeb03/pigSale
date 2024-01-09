import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from '../views/LoginView.vue'
import LoginForm from '@/components/user/LoginForm.vue'
import MyRegister from '@/components/user/MyRegister.vue'
import UserHomeView from '../views/UserHomeView.vue'
import SearchResult from '../views/GoodsView.vue'
import GoodsDetails from '@/components/goods/GoodsDetails.vue'
import Review from '../components/review/Review.vue'
import UserInfo from '../views/UserInfoView.vue'
import ShoppingCart from '../components/shopping-cart/ShoppingCart.vue'
import SubmitOrder from '../components/orders/SubmitOrder.vue'
import MyOrders from '../components/orders/MyOrders.vue'
import AddressTable from '../components/user-info/AddressTable.vue'
import UserReview from '../components/review/UserReview.vue'
import UserData from '../components/user-info/UserData.vue'
import EditUserData from '../components/user-info/EditUserData.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/userHome' },
    // { path: '/', redirect: '/userInfo' },
    {
        path: '/',
        component: LoginView,
        children: [
            { path: 'login', component: LoginForm },
            { path: 'register', component: MyRegister },
        ]
    },
    { path: '/userHome', component: UserHomeView },
    { path: '/search/:keyword', component: SearchResult },
    { path: '/search', component: SearchResult },
    { path: '/goods/:productId', component: GoodsDetails },
    { path: '/review', name: "review", component: Review },
    { path: '/userInfo', redirect: '/userInfo/-1' },
    {
        path: '/userInfo/:loginID',
        redirect: '/userInfo/:loginID/cart',
        name: 'userInfo',
        component: UserInfo,
        children: [
            { path: 'cart', component: ShoppingCart },
            { path: 'orders', component: MyOrders },
            { path: 'submit', component: SubmitOrder },
            { path: 'address', component: AddressTable },
            { path: 'review', component: UserReview },
            { path: 'data', component: UserData },
            { path: 'edit', component: EditUserData },
        ],
    }
]

const router = new VueRouter({
    routes
})

//拦截进入userInfo
router.beforeEach((to, from, next) => {
    // if (to.path !== '/login' && to.path !== '/userHome' && to.path !== '/search') {
    //     const token = localStorage.getItem('loginID')
    //     if (!token) {
    //         next('/login')
    //     }
    // }
    next()
})

export default router






