<template>
  <div style="max-width:600px;margin:24px auto;padding:0 24px">
    <h2>{{ $t('payment.title') }}</h2>

    <template v-if="!payResult">
      <el-card v-if="order" style="margin-bottom:24px">
        <div style="display:flex;justify-content:space-between;margin-bottom:12px">
          <span style="color:#666">{{ $t('payment.orderNo') }}</span>
          <span style="font-weight:bold">{{ order.orderNo }}</span>
        </div>
        <div style="display:flex;justify-content:space-between">
          <span style="color:#666">{{ $t('payment.amount') }}</span>
          <span style="color:var(--primary);font-size:24px;font-weight:bold">￥{{ order.totalAmount }}</span>
        </div>
      </el-card>

      <el-card style="margin-bottom:24px">
        <h4 style="margin-top:0">{{ $t('payment.method') }}</h4>
        <el-radio-group v-model="payType">
          <el-radio value="ALIPAY" size="large">{{ $t('payment.alipay') }}</el-radio>
          <el-radio value="WECHAT" size="large">{{ $t('payment.wechat') }}</el-radio>
        </el-radio-group>
      </el-card>

      <el-button type="primary" size="large" style="width:100%" @click="pay" :loading="paying">
        {{ $t('payment.pay') }} ￥{{ order?.totalAmount }}
      </el-button>
    </template>

    <template v-else>
      <el-result v-if="payResult==='success'" status="success" :title="$t('payment.success')">
        <template #extra>
          <el-button type="primary" @click="$router.push('/orders')">{{ $t('payment.viewOrders') }}</el-button>
        </template>
      </el-result>
      <el-result v-else status="error" :title="$t('payment.failed')">
        <template #extra>
          <el-button type="primary" @click="retryPayment">{{ $t('payment.retry') }}</el-button>
        </template>
      </el-result>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderDetail } from '@/api/order'
import { createPayment } from '@/api/payment'

const route = useRoute()
const order = ref<any>(null)
const payType = ref('ALIPAY')
const paying = ref(false)
const payResult = ref<string>('')

function retryPayment() {
  payResult.value = ''
}

async function pay() {
  paying.value = true
  try {
    await createPayment({ orderId: order.value.id, payType: payType.value })
    payResult.value = 'success'
    ElMessage.success('Payment successful')
  } catch {
    payResult.value = 'failed'
    ElMessage.error('Payment failed')
  } finally {
    paying.value = false
  }
}

onMounted(async () => {
  try {
    const res: any = await getOrderDetail(Number(route.params.orderId))
    order.value = res.data
  } catch {
    ElMessage.error('Failed to load order')
  }
})
</script>
