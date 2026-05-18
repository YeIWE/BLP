import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', name: 'Home', component: () => import('@/views/home/index.vue') },
    { path: '/product', name: 'ProductList', component: () => import('@/views/product/list.vue') },
    { path: '/product/:id', name: 'ProductDetail', component: () => import('@/views/product/detail.vue') },
  ],
})

export default router
