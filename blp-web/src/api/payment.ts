import request from './request'

export function createPayment(data: { orderId: number; payType: string }) { return request.post('/payment/pay', data) }
