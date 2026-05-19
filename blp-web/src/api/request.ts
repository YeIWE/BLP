import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({ baseURL: '/api', timeout: 15000 })

request.interceptors.request.use(config => {
  const token = localStorage.getItem('web_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  res => res.data,
  err => {
    if (err.response?.status === 401) {
      // Don't show error for public pages; silently fail
      if (window.location.hash.includes('/cart') || window.location.hash.includes('/checkout') ||
          window.location.hash.includes('/payment') || window.location.hash.includes('/orders')) {
        ElMessage.warning('请先登录')
        window.location.hash = '#/login'
      }
    } else {
      const msg = err.response?.data?.message || err.message || '网络请求失败'
      ElMessage.error(msg)
    }
    return Promise.reject(err)
  }
)

export default request
