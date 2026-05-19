import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as apiLogin, getUserMenus } from '@/api/auth'
import router from '@/router'

interface MenuItem {
  id: number; name: string; path: string; component: string; icon: string;
  type: number; parentId: number; children?: MenuItem[];
}

const viewMap: Record<string, () => Promise<any>> = {
  'sys/user': () => import('@/views/sys/user/index.vue'),
  'sys/role': () => import('@/views/sys/role/index.vue'),
  'sys/menu': () => import('@/views/sys/menu/index.vue'),
  'pms/product': () => import('@/views/pms/product/index.vue'),
  'pms/category': () => import('@/views/pms/category/index.vue'),
  'pms/brand': () => import('@/views/pms/brand/index.vue'),
  'oms/order': () => import('@/views/oms/order/index.vue'),
  'prom/seckill': () => import('@/views/prom/seckill/index.vue'),
}

const defaultMenus: MenuItem[] = [
  { id: 1, parentId: 0, name: '系统管理', path: '/sys', component: '', icon: 'Setting', type: 0, children: [
    { id: 2, parentId: 1, name: '用户管理', path: '/sys/user', component: 'sys/user/index', icon: 'User', type: 1 },
    { id: 3, parentId: 1, name: '角色管理', path: '/sys/role', component: 'sys/role/index', icon: 'UserFilled', type: 1 },
    { id: 4, parentId: 1, name: '菜单管理', path: '/sys/menu', component: 'sys/menu/index', icon: 'Menu', type: 1 },
  ]},
  { id: 5, parentId: 0, name: '商品管理', path: '/pms', component: '', icon: 'Goods', type: 0, children: [
    { id: 6, parentId: 5, name: '商品列表', path: '/pms/product', component: 'pms/product/index', icon: 'Goods', type: 1 },
    { id: 7, parentId: 5, name: '分类管理', path: '/pms/category', component: 'pms/category/index', icon: 'Operation', type: 1 },
    { id: 8, parentId: 5, name: '品牌管理', path: '/pms/brand', component: 'pms/brand/index', icon: 'Medal', type: 1 },
  ]},
  { id: 9, parentId: 0, name: '订单管理', path: '/oms', component: '', icon: 'Document', type: 0, children: [
    { id: 10, parentId: 9, name: '订单列表', path: '/oms/order', component: 'oms/order/index', icon: 'Document', type: 1 },
  ]},
  { id: 11, parentId: 0, name: '营销管理', path: '/prom', component: '', icon: 'Timer', type: 0, children: [
    { id: 12, parentId: 11, name: '秒杀管理', path: '/prom/seckill', component: 'prom/seckill/index', icon: 'Timer', type: 1 },
  ]},
]

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<any>(null)
  const menus = ref<MenuItem[]>(defaultMenus)

  async function login(username: string, password: string) {
    const res: any = await apiLogin(username, password)
    token.value = res.data.access_token
    localStorage.setItem('token', res.data.access_token)
    await loadMenus()
    router.push('/dashboard')
  }

  async function loadMenus() {
    try {
      const res: any = await getUserMenus()
      if (res?.data && Array.isArray(res.data) && res.data.length > 0) {
        menus.value = res.data
      } else {
        menus.value = defaultMenus
      }
    } catch {
      menus.value = defaultMenus
    }
  }

  function logout() {
    token.value = ''; userInfo.value = null; menus.value = []
    localStorage.removeItem('token')
    router.push('/login')
  }

  return { token, userInfo, menus, login, logout, loadMenus, viewMap }
})
