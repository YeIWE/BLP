import request from './request'

export function getProductPage(params: any) { return request.get('/product/page', { params }) }
export function createProduct(data: any) { return request.post('/product', data) }
export function updateProduct(id: number, data: any) { return request.put(`/product/${id}`, data) }
export function deleteProduct(id: number) { return request.delete(`/product/${id}`) }
export function uploadImage(file: File) {
  const fd = new FormData(); fd.append('file', file)
  return request.post('/product/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
}
export function getCategoryTree() { return request.get('/product/category/tree') }
export function createCategory(data: any) { return request.post('/product/category', data) }
export function updateCategory(id: number, data: any) { return request.put(`/product/category/${id}`, data) }
export function deleteCategory(id: number) { return request.delete(`/product/category/${id}`) }
export function getBrandPage(params: any) { return request.get('/product/brand/page', { params }) }
export function createBrand(data: any) { return request.post('/product/brand', data) }
export function updateBrand(id: number, data: any) { return request.put(`/product/brand/${id}`, data) }
export function deleteBrand(id: number) { return request.delete(`/product/brand/${id}`) }
