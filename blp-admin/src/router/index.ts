import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue') },
    {
      path: '/', name: 'Layout', component: () => import('@/layout/index.vue'), redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '首页', icon: 'HomeFilled' } },
        { path: 'sys/user', name: 'SysUser', component: () => import('@/views/sys/user/index.vue'), meta: { title: '用户管理', icon: 'User' } },
        { path: 'sys/role', name: 'SysRole', component: () => import('@/views/sys/role/index.vue'), meta: { title: '角色管理', icon: 'UserFilled' } },
        { path: 'sys/menu', name: 'SysMenu', component: () => import('@/views/sys/menu/index.vue'), meta: { title: '菜单管理', icon: 'Menu' } },
        { path: 'pms/product', name: 'PmsProduct', component: () => import('@/views/pms/product/index.vue'), meta: { title: '商品列表', icon: 'Goods' } },
        { path: 'pms/category', name: 'PmsCategory', component: () => import('@/views/pms/category/index.vue'), meta: { title: '分类管理', icon: 'Operation' } },
        { path: 'pms/brand', name: 'PmsBrand', component: () => import('@/views/pms/brand/index.vue'), meta: { title: '品牌管理', icon: 'Medal' } },
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
