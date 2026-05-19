import request from './request'

export function searchProducts(params: any) { return request.get('/search', { params }) }
