import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'Home', component: () => import('@/views/home/index.vue') },
    { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue') },
    { path: '/product', name: 'ProductList', component: () => import('@/views/product/list.vue') },
    { path: '/product/:id', name: 'ProductDetail', component: () => import('@/views/product/detail.vue') },
    { path: '/cart', name: 'Cart', component: () => import('@/views/cart/index.vue'), meta: { requireAuth: true } },
    { path: '/checkout', name: 'Checkout', component: () => import('@/views/checkout/index.vue'), meta: { requireAuth: true } },
    { path: '/payment/:orderId', name: 'Payment', component: () => import('@/views/payment/index.vue'), meta: { requireAuth: true } },
    { path: '/orders', name: 'Orders', component: () => import('@/views/order/index.vue'), meta: { requireAuth: true } },
    { path: '/seckill', name: 'Seckill', component: () => import('@/views/seckill/index.vue') },
  ],
})

router.beforeEach((to, _from, next) => {
  if (to.meta.requireAuth) {
    const token = localStorage.getItem('web_token')
    if (!token) { next('/login'); return }
  }
  next()
})

export default router
