<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <div style="display:flex;gap:12px">
          <el-input v-model="keyword" :placeholder="$t('common.search')" style="width:240px" clearable @clear="fetch" @keyup.enter="fetch" />
          <el-select v-model="statusFilter" :placeholder="$t('common.status')" style="width:160px" clearable @change="fetch">
            <el-option label="PENDING" value="PENDING" />
            <el-option label="PAID" value="PAID" />
            <el-option label="SHIPPED" value="SHIPPED" />
            <el-option label="DELIVERED" value="DELIVERED" />
            <el-option label="CANCELLED" value="CANCELLED" />
          </el-select>
        </div>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="orderNo" :label="$t('order.orderNo')" width="180" />
        <el-table-column prop="username" :label="$t('order.username')" width="120" />
        <el-table-column prop="totalAmount" :label="$t('order.totalAmount')" width="120">
          <template #default="{row}">￥{{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('common.status')" width="120">
          <template #default="{row}">
            <el-tag :type="statusTagType(row.status)">{{ $t('order.status.' + row.status.toLowerCase()) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" :label="$t('order.receiverName')" width="120" />
        <el-table-column prop="createTime" :label="$t('order.createTime')" width="180" />
        <el-table-column :label="$t('common.actions')" min-width="200">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDetail(row)">{{ $t('common.view') }}</el-button>
            <el-button v-if="row.status==='PAID'" type="success" size="small" @click="changeStatus(row, 'SHIPPED')">{{ $t('order.ship') }}</el-button>
            <el-button v-if="row.status==='SHIPPED'" type="success" size="small" @click="changeStatus(row, 'DELIVERED')">{{ $t('order.deliver') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" background layout="total,prev,pager,next" :total="total" v-model:current-page="page" @current-change="fetch" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="$t('order.detail')" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item :label="$t('order.orderNo')">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.username')">{{ detail.username }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.totalAmount')">￥{{ detail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item :label="$t('common.status')">
            <el-tag :type="statusTagType(detail.status)">{{ $t('order.status.' + detail.status.toLowerCase()) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="$t('order.receiverName')">{{ detail.receiverName }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.receiverPhone')">{{ detail.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.receiverAddress')" :span="2">{{ detail.receiverAddress }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.createTime')">{{ detail.createTime }}</el-descriptions-item>
          <el-descriptions-item :label="$t('order.paymentTime')">{{ detail.paymentTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin-top:20px">{{ $t('order.items') }}</h4>
        <el-table :data="detail.orderItems||[]" border size="small">
          <el-table-column prop="productName" :label="$t('product.name')" />
          <el-table-column prop="productPrice" :label="$t('product.price')" width="100">
            <template #default="{row}">￥{{ row.productPrice }}</template>
          </el-table-column>
          <el-table-column prop="quantity" :label="$t('order.quantity')" width="80" />
          <el-table-column :label="$t('order.subtotal')" width="100">
            <template #default="{row}">￥{{ row.productPrice * row.quantity }}</template>
          </el-table-column>
        </el-table>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderPage, getOrderDetail, updateOrderStatus } from '@/api/order'

const { t } = useI18n()

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const keyword = ref('')
const statusFilter = ref('')
const dialogVisible = ref(false)
const detail = ref<any>(null)

function statusTagType(status: string) {
  const map: Record<string, string> = { PENDING: 'info', PAID: 'warning', SHIPPED: '', DELIVERED: 'success', CANCELLED: 'danger' }
  return map[status] || 'info'
}

async function fetch() {
  loading.value = true
  try {
    const params: any = { page: page.value, size: 10, keyword: keyword.value }
    if (statusFilter.value) params.status = statusFilter.value
    const res: any = await getOrderPage(params)
    tableData.value = res.data.records; total.value = res.data.total
  } finally { loading.value = false }
}

async function openDetail(row: any) {
  try {
    const res: any = await getOrderDetail(row.id)
    detail.value = res.data
    dialogVisible.value = true
  } catch {}
}

async function changeStatus(row: any, newStatus: string) {
  try {
    await ElMessageBox.confirm(`${t('order.confirmStatus')} "${t('order.status.' + newStatus.toLowerCase())}"?`, t('common.confirm'), { type: 'warning' })
    await updateOrderStatus(row.id, newStatus)
    ElMessage.success(t('common.success'))
    fetch()
  } catch {}
}

fetch()
</script>
