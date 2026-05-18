import request from './request'

export function getOrderPage(params: any) { return request.get('/order/page', { params }) }
export function getOrderDetail(id: number) { return request.get(`/order/${id}`) }
export function updateOrderStatus(id: number, status: string) { return request.put(`/order/${id}/status`, { status }) }
