import request from './request'

export function getReviewPage(params: any) { return request.get('/review/page', { params }) }
export function getReviewStats(productId: number) { return request.get(`/review/stats/${productId}`) }
