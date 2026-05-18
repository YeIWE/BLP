import request from './request'

export function getProductPage(params: any) { return request.get('/product/page', { params }) }
export function getProductDetail(id: number) { return request.get(`/product/${id}`) }
export function getCategoryTree() { return request.get('/product/category/tree') }
