import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue') },
    {
      path: '/', name: 'Layout', component: () => import('@/layout/index.vue'), redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '首页', icon: 'HomeFilled' } },
        { path: 'oms/order', name: 'OrderList', component: () => import('@/views/oms/order/index.vue'), meta: { title: '订单管理', icon: 'Document' } },
        { path: 'prom/seckill', name: 'SeckillList', component: () => import('@/views/prom/seckill/index.vue'), meta: { title: '秒杀管理', icon: 'Timer' } },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/login' },
  ],
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) next('/login')
  else next()
})

export default router
