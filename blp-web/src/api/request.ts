import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({ baseURL: '/api', timeout: 15000 })

request.interceptors.response.use(
  res => res.data,
  err => {
    const msg = err.response?.data?.message || err.message || '网络请求失败，请检查服务是否启动'
    ElMessage.error(msg)
    return Promise.reject(err)
  }
)

export default request
