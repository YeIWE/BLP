import request from './request'

export function getSeckillList(params?: any) { return request.get('/promotion/seckill/list', { params }) }
export function buySeckill(id: number) { return request.post(`/promotion/seckill/${id}/buy`) }
