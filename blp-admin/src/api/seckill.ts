import request from './request'

export function getSeckillList(params?: any) { return request.get('/promotion/seckill/page', { params }) }
export function createSeckill(data: any) { return request.post('/promotion/seckill', data) }
export function deleteSeckill(id: number) { return request.delete(`/promotion/seckill/${id}`) }
