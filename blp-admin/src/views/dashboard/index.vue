<template>
  <div>
    <h2 style="margin-bottom:20px">{{ $t('dashboard.welcome') }}</h2>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="今日订单" :value="stats.todayOrders" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="今日营收" :value="stats.todayRevenue"><template #prefix>¥</template></el-statistic></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="商品总数" :value="stats.totalProducts" /></el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><el-statistic title="用户总数" :value="stats.totalUsers" /></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12"><el-card><div ref="chartOrder" style="height:300px" /></el-card></el-col>
      <el-col :span="12"><el-card><div ref="chartRevenue" style="height:300px" /></el-card></el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12"><el-card><div ref="chartCategory" style="height:300px" /></el-card></el-col>
      <el-col :span="12"><el-card><div ref="chartUser" style="height:300px" /></el-card></el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboardStats } from '@/api/dashboard'

const stats = ref({ todayOrders: 0, todayRevenue: 0, totalProducts: 0, totalUsers: 0 })
const chartOrder = ref<HTMLDivElement>()
const chartRevenue = ref<HTMLDivElement>()
const chartCategory = ref<HTMLDivElement>()
const chartUser = ref<HTMLDivElement>()
const charts: echarts.ECharts[] = []
const themeColor = '#1890FF'

async function loadStats() {
  try {
    const res: any = await getDashboardStats()
    if (res?.data) stats.value = res.data
  } catch {}
}

function initCharts() {
  nextTick(() => {
    const days = ['周一','周二','周三','周四','周五','周六','周日']
    const orderData = [12,19,15,25,22,30,18]
    const revenueData = [1200,1900,1500,2500,2200,3000,1800]

    if (chartOrder.value) {
      const c = echarts.init(chartOrder.value)
      c.setOption({
        title: { text: '近7天订单趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { data: days }, yAxis: {},
        series: [{ name: '订单数', type: 'line', data: orderData, smooth: true, areaStyle: { opacity: 0.2 } }],
        color: [themeColor]
      })
      charts.push(c)
    }
    if (chartRevenue.value) {
      const c = echarts.init(chartRevenue.value)
      c.setOption({
        title: { text: '近7天营收趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { data: days }, yAxis: {},
        series: [{ name: '营收(元)', type: 'bar', data: revenueData, itemStyle: { borderRadius: [4,4,0,0] } }],
        color: ['#52C41A']
      })
      charts.push(c)
    }
    if (chartCategory.value) {
      const c = echarts.init(chartCategory.value)
      c.setOption({
        title: { text: '商品分类占比', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie', radius: ['40%','70%'],
          data: [{name:'电子产品',value:35},{name:'服装',value:25},{name:'食品',value:20},{name:'家居',value:15},{name:'其他',value:5}],
        }]
      })
      charts.push(c)
    }
    if (chartUser.value) {
      const c = echarts.init(chartUser.value)
      c.setOption({
        title: { text: '用户增长趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { data: days }, yAxis: {},
        series: [{ name: '新增用户', type: 'line', data: [5,8,12,7,15,10,20], smooth: true }],
        color: ['#722ED1']
      })
      charts.push(c)
    }
  })
}

let timer: any
onMounted(() => {
  loadStats()
  initCharts()
  timer = setInterval(loadStats, 15000)
  window.addEventListener('resize', () => charts.forEach(c => c.resize()))
})
onUnmounted(() => {
  clearInterval(timer)
  charts.forEach(c => c.dispose())
})
</script>
