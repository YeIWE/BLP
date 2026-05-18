import request from './request'

export function login(username: string, password: string) {
  const params = new URLSearchParams()
  params.append('grant_type', 'password')
  params.append('username', username)
  params.append('password', password)
  params.append('scope', 'all')
  return request.post('/auth/oauth2/token', params, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    auth: { username: 'blp-admin', password: 'blp-secret' },
  })
}

export function getUserMenus() {
  return request.get('/user/menu/user/1')
}
