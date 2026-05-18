<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <div />
        <el-button type="primary" @click="openCreateDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="productId" :label="$t('seckill.productId')" width="100" />
        <el-table-column prop="seckillPrice" :label="$t('seckill.seckillPrice')" width="120">
          <template #default="{row}">￥{{ row.seckillPrice }}</template>
        </el-table-column>
        <el-table-column prop="stock" :label="$t('seckill.stock')" width="100" />
        <el-table-column prop="startTime" :label="$t('seckill.startTime')" width="180" />
        <el-table-column prop="endTime" :label="$t('seckill.endTime')" width="180" />
        <el-table-column :label="$t('common.status')" width="100">
          <template #default="{row}">
            <el-tag :type="seckillStatus(row) === 'active' ? 'success' : 'info'">
              {{ seckillStatus(row) === 'active' ? $t('common.enabled') : $t('common.disabled') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="120">
          <template #default="{row}">
            <el-button type="danger" size="small" @click="handleDelete(row.id)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="$t('common.create')" width="500px">
      <el-form :model="form">
        <el-form-item :label="$t('seckill.productId')">
          <el-input-number v-model="form.productId" style="width:100%" :min="1" />
        </el-form-item>
        <el-form-item :label="$t('seckill.seckillPrice')">
          <el-input-number v-model="form.seckillPrice" :precision="2" :step="0.01" style="width:100%" :min="0.01" />
        </el-form-item>
        <el-form-item :label="$t('seckill.stock')">
          <el-input-number v-model="form.stock" style="width:100%" :min="1" />
        </el-form-item>
        <el-form-item :label="$t('seckill.startTime')">
          <el-date-picker v-model="form.startTime" type="datetime" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('seckill.endTime')">
          <el-date-picker v-model="form.endTime" type="datetime" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSeckillList, createSeckill, deleteSeckill } from '@/api/seckill'

const { t } = useI18n()

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({
  productId: undefined as number | undefined,
  seckillPrice: 0,
  stock: 0,
  startTime: '',
  endTime: '',
})

function seckillStatus(row: any) {
  if (!row.startTime || !row.endTime) return 'inactive'
  const now = new Date().getTime()
  const start = new Date(row.startTime).getTime()
  const end = new Date(row.endTime).getTime()
  return now >= start && now <= end ? 'active' : 'inactive'
}

async function fetch() {
  loading.value = true
  try {
    const res: any = await getSeckillList()
    tableData.value = res.data?.records || res.data || []
  } finally { loading.value = false }
}

function openCreateDialog() {
  form.productId = undefined
  form.seckillPrice = 0
  form.stock = 0
  form.startTime = ''
  form.endTime = ''
  dialogVisible.value = true
}

async function handleSave() {
  try {
    const data: any = {
      productId: form.productId,
      seckillPrice: form.seckillPrice,
      stock: form.stock,
    }
    if (form.startTime) {
      data.startTime = typeof form.startTime === 'string' ? form.startTime : (form.startTime as Date).toISOString()
    }
    if (form.endTime) {
      data.endTime = typeof form.endTime === 'string' ? form.endTime : (form.endTime as Date).toISOString()
    }
    await createSeckill(data)
    ElMessage.success(t('common.success'))
    dialogVisible.value = false
    fetch()
  } catch {}
}

async function handleDelete(id: number) {
  try {
    await ElMessageBox.confirm(t('common.confirm'), t('common.delete'), { type: 'warning' })
    await deleteSeckill(id)
    ElMessage.success(t('common.success'))
    fetch()
  } catch {}
}

fetch()
</script>
