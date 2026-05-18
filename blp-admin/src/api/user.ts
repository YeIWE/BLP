import request from './request'

export function getUserPage(params: any) { return request.get('/user/page', { params }) }
export function createUser(data: any) { return request.post('/user', data) }
export function updateUser(id: number, data: any) { return request.put(`/user/${id}`, data) }
export function deleteUser(id: number) { return request.delete(`/user/${id}`) }
export function getRoleList() { return request.get('/user/role') }
export function createRole(data: any) { return request.post('/user/role', data) }
export function updateRole(id: number, data: any) { return request.put(`/user/role/${id}`, data) }
export function deleteRole(id: number) { return request.delete(`/user/role/${id}`) }
export function getMenuTree() { return request.get('/user/menu/tree') }
export function createMenu(data: any) { return request.post('/menu', data) }
export function updateMenu(id: number, data: any) { return request.put(`/menu/${id}`, data) }
export function deleteMenu(id: number) { return request.delete(`/menu/${id}`) }
