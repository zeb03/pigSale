import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home";

Vue.use(VueRouter)

const routes = [
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/',
    name: 'Home',
    component: Home,
    children:[
      {
        path: '/user/review',
        name: 'UserReview',
        component: () => import('@/views/UserReview')
      },
      {
        path: '/review',
        name: 'Review',
        component: () => import('@/views/Review')
      },
    ]
  },
  {
    path: '/review',
    name: 'Review',
    component: () => import('@/views/Review')
  },
  {
    path: '/user/review',
    name: 'UserReview',
    component: () => import('@/views/UserReview')
  },
  {
    path: '/permission',
    name: 'Permission',
    component: () => import('@/views/MyPermission')
  },
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
