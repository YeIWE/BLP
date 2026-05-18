import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as apiLogin, getUserMenus } from '@/api/auth'
import router from '@/router'
import type { RouteRecordRaw } from 'vue-router'

interface MenuItem {
  id: number; name: string; path: string; component: string; icon: string;
  type: number; parentId: number; children?: MenuItem[];
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<any>(null)
  const menus = ref<MenuItem[]>([])

  async function login(username: string, password: string) {
    const res: any = await apiLogin(username, password)
    token.value = res.data.access_token
    localStorage.setItem('token', res.data.access_token)
    await loadMenus()
  }

  async function loadMenus() {
    const res: any = await getUserMenus()
    menus.value = res.data || []
    addDynamicRoutes(menus.value)
  }

  function addDynamicRoutes(menuList: MenuItem[]) {
    menuList.filter(m => m.type === 1 && m.component).forEach(m => {
      const route: RouteRecordRaw = {
        path: m.path, name: m.name,
        component: () => {
          const comp = m.component.replace('index', 'Index')
          const parts = comp.split('/')
          return import(`@/views/${parts.join('/')}.vue`)
        },
        meta: { title: m.name, icon: m.icon },
      }
      router.addRoute('Layout', route)
    })
  }

  function logout() {
    token.value = ''; userInfo.value = null; menus.value = []
    localStorage.removeItem('token')
    router.push('/login')
  }

  return { token, userInfo, menus, login, logout, loadMenus }
})
