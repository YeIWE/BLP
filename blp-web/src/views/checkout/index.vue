<template>
  <div style="max-width:800px;margin:24px auto;padding:0 24px">
    <h2>{{ $t('checkout.title') }}</h2>

    <div v-if="checkoutItems.length > 0" style="margin-bottom:24px">
      <h4>Order Summary</h4>
      <el-card v-for="item in checkoutItems" :key="item.productId" style="margin-bottom:8px">
        <div style="display:flex;align-items:center;gap:16px">
          <img :src="item.productImage||'https://placehold.co/60x60?text=Product'" style="width:60px;height:60px;object-fit:cover;border-radius:4px" />
          <span style="flex:1;font-weight:bold">{{ item.productName }}</span>
          <span style="color:#999">x{{ item.quantity }}</span>
          <span style="color:var(--primary);font-weight:bold">￥{{ (item.price * item.quantity).toFixed(2) }}</span>
        </div>
      </el-card>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('checkout.receiver')" prop="receiverName">
        <el-input v-model="form.receiverName" />
      </el-form-item>
      <el-form-item :label="$t('checkout.phone')" prop="receiverPhone">
        <el-input v-model="form.receiverPhone" />
      </el-form-item>
      <el-form-item :label="$t('checkout.address')" prop="receiverAddress">
        <el-input v-model="form.receiverAddress" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item>
        <div style="font-size:14px;color:#666">{{ $t('cart.total') }}: <span style="color:var(--primary);font-size:28px;font-weight:bold">￥{{ totalAmount.toFixed(2) }}</span></div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="large" @click="placeOrder" :loading="submitting">{{ $t('checkout.submit') }}</el-button>
      </el-form-item>
    </el-form>

    <el-empty v-if="checkoutItems.length===0" description="No items to checkout" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createOrder } from '@/api/order'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)
const checkoutItems = ref<any[]>([])

const form = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
})

const rules = {
  receiverName: [{ required: true, message: 'Receiver name is required', trigger: 'blur' }],
  receiverPhone: [{ required: true, message: 'Phone is required', trigger: 'blur' }],
  receiverAddress: [{ required: true, message: 'Address is required', trigger: 'blur' }],
}

const totalAmount = computed(() => {
  return checkoutItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

async function placeOrder() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const orderItems = checkoutItems.value.map(item => ({
      productId: item.productId,
      productName: item.productName,
      productImage: item.productImage,
      price: item.price,
      quantity: item.quantity,
    }))
    const res: any = await createOrder({
      ...form.value,
      items: orderItems,
      totalAmount: totalAmount.value,
    })
    ElMessage.success('Order placed successfully')
    sessionStorage.removeItem('checkoutItems')
    sessionStorage.removeItem('checkoutCartData')
    router.push(`/payment/${res.data?.id||res.data}`)
  } catch {
    ElMessage.error('Failed to place order')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  const stored = sessionStorage.getItem('checkoutCartData')
  if (stored) {
    try { checkoutItems.value = JSON.parse(stored) } catch {}
  }
})
</script>
