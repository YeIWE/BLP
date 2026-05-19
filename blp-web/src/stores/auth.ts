import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as apiLogin } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('web_token') || '')
  const userInfo = ref<any>(null)

  async function login(username: string, password: string) {
    const res: any = await apiLogin(username, password)
    token.value = res.data.access_token
    localStorage.setItem('web_token', res.data.access_token)
    userInfo.value = { username }
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('web_token')
    router.push('/')
  }

  return { token, userInfo, login, logout }
})
