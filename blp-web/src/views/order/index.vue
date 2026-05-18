<template>
  <div style="max-width:1200px;margin:24px auto;padding:0 24px">
    <h2>{{ $t('orders.title') }}</h2>

    <el-table :data="orders" row-key="id" @expand-change="handleExpand">
      <el-table-column type="expand">
        <template #default="{ row }">
          <div style="padding:16px">
            <h4 style="margin:0 0 12px">Order Items</h4>
            <el-table :data="row.items||[]" size="small" style="width:100%">
              <el-table-column label="" width="80">
                <template #default="{ row: r }">
                  <img :src="r.productImage||'https://placehold.co/50x50?text=Product'" style="width:50px;height:50px;object-fit:cover;border-radius:4px" />
                </template>
              </el-table-column>
              <el-table-column prop="productName" label="" />
              <el-table-column label="" width="100">
                <template #default="{ row: r }">￥{{ r.price }}</template>
              </el-table-column>
              <el-table-column prop="quantity" label="" width="80" />
              <el-table-column label="" width="120">
                <template #default="{ row: r }">￥{{ (r.price * r.quantity).toFixed(2) }}</template>
              </el-table-column>
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="orderNo" label="" />
      <el-table-column label="" width="150">
        <template #default="{ row }">￥{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column label="" width="120">
        <template #default="{ row }">
          <el-tag v-if="row.status==='PENDING'" type="warning" size="small">Pending</el-tag>
          <el-tag v-else-if="row.status==='PAID'" type="success" size="small">Paid</el-tag>
          <el-tag v-else-if="row.status==='CANCELLED'" type="danger" size="small">Cancelled</el-tag>
          <el-tag v-else-if="row.status==='SHIPPED'" type="primary" size="small">Shipped</el-tag>
          <el-tag v-else-if="row.status==='COMPLETED'" type="success" size="small">Completed</el-tag>
          <el-tag v-else size="small" type="info">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="" width="180" />
    </el-table>

    <el-pagination
      style="margin-top:24px;justify-content:center"
      background
      layout="prev,next"
      :total="total"
      v-model:current-page="page"
      @current-change="fetchOrders"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOrderPage } from '@/api/order'

const orders = ref<any[]>([])
const page = ref(1)
const total = ref(0)

async function fetchOrders() {
  try {
    const res: any = await getOrderPage({ page: page.value, size: 10 })
    orders.value = (res.data?.records||[]).map((o:any) => ({ ...o, items: o.items||[] }))
    total.value = res.data?.total||0
  } catch {}
}

function handleExpand(row: any, expandedRows: any[]) {
  // items already loaded from the order record
}

onMounted(() => { fetchOrders() })
</script>
