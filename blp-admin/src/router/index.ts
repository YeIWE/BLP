import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/login/index.vue') },
    {
      path: '/', name: 'Layout', component: () => import('@/layout/index.vue'), redirect: '/dashboard',
      children: [
        { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/index.vue'), meta: { title: '首页', icon: 'HomeFilled' } },
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
