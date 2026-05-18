<template>
  <div>
    <h2>{{ $t('dashboard.welcome') }}</h2>
    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-row :gutter="20" style="margin-top:20px">
          <el-col :span="6" v-for="i in 4" :key="i">
            <el-card><el-skeleton-item variant="text" style="width:50%" /><el-skeleton-item variant="text" style="width:30%" /></el-card>
          </el-col>
        </el-row>
      </template>
      <el-row :gutter="20" style="margin-top:20px">
        <el-col :span="6">
          <el-card>
            <el-statistic :title="$t('dashboard.todayOrders')" :value="stats.todayOrders" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic :title="$t('dashboard.todayRevenue')" :value="stats.todayRevenue">
              <template #prefix>￥</template>
            </el-statistic>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic :title="$t('dashboard.totalProducts')" :value="stats.totalProducts" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic :title="$t('dashboard.totalUsers')" :value="stats.totalUsers" />
          </el-card>
        </el-col>
      </el-row>
    </el-skeleton>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { getDashboardStats } from '@/api/dashboard'

const loading = ref(true)
const stats = reactive({
  todayOrders: 0,
  todayRevenue: 0,
  totalProducts: 0,
  totalUsers: 0,
})

let timer: ReturnType<typeof setInterval> | null = null

async function fetch() {
  try {
    const res: any = await getDashboardStats()
    const data = res.data || res
    stats.todayOrders = data.todayOrders || 0
    stats.todayRevenue = data.todayRevenue || 0
    stats.totalProducts = data.totalProducts || 0
    stats.totalUsers = data.totalUsers || 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetch()
  timer = setInterval(fetch, 10000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>
