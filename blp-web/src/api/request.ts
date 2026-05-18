import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({ baseURL: '/api', timeout: 15000 })

request.interceptors.response.use(
  res => res.data,
  err => { ElMessage.error('Request failed'); return Promise.reject(err) }
)

export default request
