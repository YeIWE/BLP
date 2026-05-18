import request from './request'

export function createOrder(data: any) { return request.post('/order', data) }
export function getOrderPage(params: any) { return request.get('/order/page', { params }) }
export function getOrderDetail(id: number) { return request.get(`/order/${id}`) }
