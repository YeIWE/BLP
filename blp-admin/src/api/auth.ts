import request from './request'

export function login(username: string, password: string) {
  return request.post('/auth/login', { username, password })
}

export function getUserMenus() {
  return request.get('/user/menu/user/1')
}
