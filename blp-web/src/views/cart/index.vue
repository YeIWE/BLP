<template>
  <div style="max-width:1200px;margin:24px auto;padding:0 24px">
    <h2>{{ $t('cart.title') }}</h2>

    <template v-if="cartItems.length > 0">
      <el-table :data="cartItems" @selection-change="handleSelectionChange" ref="tableRef" row-key="productId">
        <el-table-column type="selection" width="55" :reserve-selection="true" />
        <el-table-column :label="$t('web.detail')" width="100">
          <template #default="{ row }">
            <img :src="row.productImage||'https://placehold.co/60x60?text=Product'" style="width:60px;height:60px;object-fit:cover;border-radius:4px" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="" />
        <el-table-column :label="$t('web.price')" width="150">
          <template #default="{ row }">￥{{ row.price }}</template>
        </el-table-column>
        <el-table-column label="" width="180">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" :max="999" size="small" @change="handleQuantityChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="" width="150">
          <template #default="{ row }">￥{{ (row.price * row.quantity).toFixed(2) }}</template>
        </el-table-column>
      </el-table>

      <div style="display:flex;align-items:center;justify-content:space-between;margin-top:24px;padding:16px;background:var(--bg-color,#fff);border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.06)">
        <div style="display:flex;align-items:center;gap:16px">
          <el-checkbox v-model="selectAll" @change="handleSelectAll" style="margin-right:8px">{{ $t('cart.selectAll') }}</el-checkbox>
          <el-button @click="removeSelected" type="danger" plain size="small" :disabled="selectedItems.length===0">{{ $t('cart.remove') }}</el-button>
        </div>
        <div style="display:flex;align-items:center;gap:16px">
          <span style="font-size:14px;color:#666">{{ $t('cart.total') }}: <span style="color:var(--primary);font-size:22px;font-weight:bold">￥{{ totalPrice.toFixed(2) }}</span></span>
          <el-button type="primary" size="large" :disabled="selectedItems.length===0" @click="goCheckout">{{ $t('cart.checkout') }}</el-button>
        </div>
      </div>
    </template>

    <el-empty v-else :description="$t('cart.empty')" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCart, updateCartItem, removeCartItem } from '@/api/cart'

const router = useRouter()
const cartItems = ref<any[]>([])
const selectedItems = ref<any[]>([])
const selectAll = ref(false)
const tableRef = ref()

const totalPrice = computed(() => {
  return selectedItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

function handleSelectionChange(val: any[]) {
  selectedItems.value = val
  selectAll.value = val.length === cartItems.value.length && cartItems.value.length > 0
}

function handleSelectAll(val: boolean) {
  if (val) {
    cartItems.value.forEach(row => { tableRef.value?.toggleRowSelection(row, true) })
  } else {
    tableRef.value?.clearSelection()
  }
}

async function handleQuantityChange(row: any) {
  try {
    await updateCartItem(row.productId, row.quantity)
  } catch {
    ElMessage.error('Update failed')
  }
}

async function removeSelected() {
  try {
    await ElMessageBox.confirm('Remove selected items?', 'Confirm', { type: 'warning' })
    for (const item of selectedItems.value) {
      await removeCartItem(item.productId)
    }
    ElMessage.success('Removed')
    await fetchCart()
  } catch {}
}

async function fetchCart() {
  try {
    const res: any = await getCart()
    cartItems.value = (res.data||[]).map((item:any) => ({ ...item, quantity: item.quantity||1 }))
  } catch {}
}

function goCheckout() {
  const ids = selectedItems.value.map((item:any) => item.productId)
  sessionStorage.setItem('checkoutItems', JSON.stringify(ids))
  sessionStorage.setItem('checkoutCartData', JSON.stringify(selectedItems.value))
  router.push('/checkout')
}

onMounted(() => { fetchCart() })
</script>
