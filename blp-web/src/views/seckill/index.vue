<template>
  <div style="max-width:1200px;margin:24px auto;padding:0 24px">
    <h2>{{ $t('seckill.title') }}</h2>

    <el-row :gutter="16" v-if="seckillItems.length > 0">
      <el-col v-for="item in seckillItems" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card shadow="hover" style="margin-bottom:16px">
          <img :src="item.productImage||'https://placehold.co/300x200?text=Seckill'" style="width:100%;height:160px;object-fit:cover;border-radius:4px" />
          <p style="font-weight:bold;margin:8px 0 4px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ item.productName }}</p>
          <p style="margin:4px 0">
            <span style="color:#999;text-decoration:line-through;font-size:13px">￥{{ item.originalPrice }}</span>
            <span style="color:var(--primary);font-size:22px;font-weight:bold;margin-left:8px">￥{{ item.seckillPrice }}</span>
          </p>
          <div style="margin:8px 0">
            <span v-if="getStatus(item)==='notStarted'" style="color:#999;font-size:13px">{{ $t('seckill.notStarted') }}</span>
            <span v-else-if="getStatus(item)==='ended'" style="color:#999;font-size:13px">{{ $t('seckill.ended') }}</span>
            <span v-else style="color:var(--primary);font-weight:bold;font-size:16px">{{ countdowns[item.id] }}</span>
          </div>
          <el-button
            type="danger"
            style="width:100%"
            :disabled="getStatus(item)!=='active' || item.stock<=0"
            @click="handleBuy(item)"
            :loading="buyingId===item.id"
          >
            {{ item.stock<=0 ? $t('seckill.soldOut') : $t('seckill.buy') }}
          </el-button>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-else :description="$t('seckill.title')" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getSeckillList, buySeckill } from '@/api/seckill'

const seckillItems = ref<any[]>([])
const buyingId = ref<number>(0)
const countdowns = ref<Record<number, string>>({})
let timer: any = null

function getStatus(item: any): 'notStarted'|'active'|'ended' {
  const now = Date.now()
  const start = new Date(item.startTime).getTime()
  const end = new Date(item.endTime).getTime()
  if (now < start) return 'notStarted'
  if (now > end) return 'ended'
  return 'active'
}

function updateCountdowns() {
  for (const item of seckillItems.value) {
    const now = Date.now()
    const start = new Date(item.startTime).getTime()
    const end = new Date(item.endTime).getTime()
    if (now < start) {
      const diff = Math.floor((start - now)/1000)
      const h = Math.floor(diff/3600)
      const m = Math.floor((diff%3600)/60)
      const s = diff%60
      countdowns.value[item.id] = `${String(h).padStart(2,'0')}:${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')}`
    } else if (now <= end) {
      const diff = Math.floor((end - now)/1000)
      const h = Math.floor(diff/3600)
      const m = Math.floor((diff%3600)/60)
      const s = diff%60
      countdowns.value[item.id] = `${String(h).padStart(2,'0')}:${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')}`
    }
  }
}

async function handleBuy(item: any) {
  buyingId.value = item.id
  try {
    await buySeckill(item.id)
    ElMessage.success('Purchase successful')
    await fetchList()
  } catch {
    ElMessage.error('Purchase failed')
  } finally {
    buyingId.value = 0
  }
}

async function fetchList() {
  try {
    const res: any = await getSeckillList()
    seckillItems.value = res.data?.records||res.data||[]
    updateCountdowns()
  } catch {}
}

onMounted(() => {
  fetchList()
  timer = setInterval(updateCountdowns, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>
