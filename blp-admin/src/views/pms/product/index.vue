<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <el-input v-model="keyword" :placeholder="$t('common.search')" style="width:240px" clearable @clear="fetch" @keyup.enter="fetch" />
        <el-button type="primary" @click="openDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" :label="$t('product.name')" />
        <el-table-column prop="price" :label="$t('product.price')" />
        <el-table-column prop="stock" :label="$t('product.stock')" />
        <el-table-column :label="$t('product.mainImage')" width="100">
          <template #default="{row}">
            <el-image v-if="row.mainImage" :src="row.mainImage" style="width:60px;height:60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.actions')" width="180">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">{{ $t('common.edit') }}</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:16px;justify-content:flex-end" background layout="total,prev,pager,next" :total="total" v-model:current-page="page" @current-change="fetch" />
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?$t('common.edit'):$t('common.create')" width="560px">
      <el-form :model="form">
        <el-form-item :label="$t('product.name')"><el-input v-model="form.name" /></el-form-item>
        <el-form-item :label="$t('product.price')"><el-input-number v-model="form.price" :precision="2" :step="0.01" style="width:100%" /></el-form-item>
        <el-form-item :label="$t('product.stock')"><el-input-number v-model="form.stock" style="width:100%" /></el-form-item>
        <el-form-item :label="$t('product.categoryId')">
          <el-tree-select v-model="form.categoryId" :data="categoryTree" :props="{label:'name',value:'id',children:'children'}" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('product.brandId')">
          <el-select v-model="form.brandId" clearable style="width:100%">
            <el-option v-for="b in brandList" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('product.description')"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item :label="$t('product.upload')">
          <el-upload :http-request="handleUpload" :show-file-list="false" accept="image/*">
            <el-button type="primary">Select Image</el-button>
          </el-upload>
          <el-image v-if="form.mainImage" :src="form.mainImage" style="width:100px;height:100px;margin-top:8px" fit="cover" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">{{ $t('common.cancel') }}</el-button><el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductPage, createProduct, updateProduct, deleteProduct, uploadImage, getCategoryTree, getBrandPage } from '@/api/product'

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const total = ref(0)
const keyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({})
const categoryTree = ref([])
const brandList = ref<any[]>([])

async function fetch() {
  loading.value = true
  try { const res: any = await getProductPage({ page: page.value, size: 10, keyword: keyword.value }); tableData.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}

async function loadOptions() {
  const [cRes, bRes]: any[] = await Promise.all([getCategoryTree(), getBrandPage({ page: 1, size: 999 })])
  categoryTree.value = cRes.data || []
  brandList.value = bRes.data?.records || []
}

function openDialog(row?: any) {
  Object.keys(form).forEach(k => delete form[k])
  if (row) { isEdit.value = true; Object.assign(form, row) }
  else isEdit.value = false
  dialogVisible.value = true
}

async function handleUpload(req: any) {
  const res: any = await uploadImage(req.file)
  form.mainImage = res.data
}

async function handleSave() {
  if (isEdit.value) { await updateProduct(form.id, form); ElMessage.success('Updated') }
  else { await createProduct(form); ElMessage.success('Created') }
  dialogVisible.value = false; fetch()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('Confirm delete?', 'Warning', { type: 'warning' })
  await deleteProduct(id); ElMessage.success('Deleted'); fetch()
}

fetch()
loadOptions()
</script>
