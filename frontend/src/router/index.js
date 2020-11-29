import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home.vue'
import User from '@/views/UserPage.vue'
import Search from '@/views/SearchPage.vue'
import Category from '@/views/CategoryPage.vue'
import ProductDetail from '@/views/ProductDetailPage.vue'
import Checkout from '@/views/CheckoutPage.vue'
import Login from '@/views/LoginPage.vue'
import About from '@/views/About.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '主页'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: '登录'
    }
  },
  {
    path: '/user',
    name: 'User',
    component: User,
    meta: {
      title: '用户'
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: User,
    meta: {
      title: '管理后台'
    }
  },
  {
    path: '/search',
    name: 'Search',
    component: Search,
    meta: {
      title: '搜索'
    }
  },
  {
    path: '/category',
    name: 'Category',
    component: Category,
    meta: {
      title: '主页'
    }
  },
  {
    path: '/product',
    name: 'ProductDetail',
    component: ProductDetail,
    meta: {
      title: '商品详情'
    }
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: Checkout,
    meta: {
      title: '收银台'
    }
  },
  {
    path: '/about',
    name: 'About',
    component: About,
    meta: {
      title: '关于'
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior() {
    return { x: 0, y: 0 }
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
