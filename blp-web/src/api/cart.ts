import request from './request'

export function getCart() { return request.get('/cart') }
export function addToCart(data: { productId: number; quantity: number }) { return request.post('/cart', data) }
export function updateCartItem(productId: number, quantity: number) { return request.put(`/cart/${productId}`, { quantity }) }
export function removeCartItem(productId: number) { return request.delete(`/cart/${productId}`) }
export function clearCart() { return request.delete('/cart') }
