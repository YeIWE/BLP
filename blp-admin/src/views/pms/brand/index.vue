<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <el-input v-model="keyword" :placeholder="$t('common.search')" style="width:240px" clearable @clear="fetch" @keyup.enter="fetch" />
        <el-button type="primary" @click="openDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" :label="$t('brand.name')" />
        <el-table-column :label="$t('brand.logo')" width="100">
          <template #default="{row}">
            <el-image v-if="row.logo" :src="row.logo" style="width:60px;height:60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('brand.description')" />
        <el-table-column :label="$t('common.actions')" width="180">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">{{ $t('common.edit') }}</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" background layout="total,prev,pager,next" :total="total" v-model:current-page="page" @current-change="fetch" />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?$t('common.edit'):$t('common.create')" width="500px">
      <el-form :model="form">
        <el-form-item :label="$t('brand.name')"><el-input v-model="form.name" /></el-form-item>
        <el-form-item :label="$t('brand.description')"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item :label="$t('brand.uploadLogo')">
          <el-upload :http-request="handleUpload" :show-file-list="false" accept="image/*">
            <el-button type="primary">Select Logo</el-button>
          </el-upload>
          <el-image v-if="form.logo" :src="form.logo" style="width:100px;height:100px;margin-top:8px" fit="cover" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">{{ $t('common.cancel') }}</el-button><el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBrandPage, createBrand, updateBrand, deleteBrand, uploadImage } from '@/api/product'

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({})

async function fetch() {
  loading.value = true
  try { const res: any = await getBrandPage({ page: page.value, size: 10, keyword: keyword.value }); tableData.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}

function openDialog(row?: any) {
  Object.keys(form).forEach(k => delete form[k])
  if (row) { isEdit.value = true; Object.assign(form, row) }
  else isEdit.value = false
  dialogVisible.value = true
}

async function handleUpload(req: any) {
  const res: any = await uploadImage(req.file)
  form.logo = res.data
}

async function handleSave() {
  if (isEdit.value) { await updateBrand(form.id, form); ElMessage.success('Updated') }
  else { await createBrand(form); ElMessage.success('Created') }
  dialogVisible.value = false; fetch()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('Confirm delete?', 'Warning', { type: 'warning' })
  await deleteBrand(id); ElMessage.success('Deleted'); fetch()
}

fetch()
</script>
