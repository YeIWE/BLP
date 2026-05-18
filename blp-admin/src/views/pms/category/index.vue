<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <span />
        <el-button type="primary" @click="openDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border row-key="id" v-loading="loading" :tree-props="{children:'children'}">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" :label="$t('category.name')" />
        <el-table-column prop="level" :label="$t('category.level')" width="80" />
        <el-table-column prop="icon" :label="$t('category.icon')" />
        <el-table-column :label="$t('common.actions')" width="180">
          <template #default="{row}">
            <el-button type="primary" size="small" @click="openDialog(row)">{{ $t('common.edit') }}</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?$t('common.edit'):$t('common.create')" width="500px">
      <el-form :model="form">
        <el-form-item :label="$t('category.name')"><el-input v-model="form.name" /></el-form-item>
        <el-form-item :label="$t('category.parentId')">
          <el-tree-select v-model="form.parentId" :data="tableData" :props="{label:'name',value:'id',children:'children'}" check-strictly clearable :placeholder="$t('category.root')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('category.icon')"><el-input v-model="form.icon" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">{{ $t('common.cancel') }}</el-button><el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryTree, createCategory, updateCategory, deleteCategory } from '@/api/product'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({})

async function fetch() {
  loading.value = true
  try { const res: any = await getCategoryTree(); tableData.value = res.data || [] }
  finally { loading.value = false }
}

function openDialog(row?: any) {
  Object.keys(form).forEach(k => delete form[k])
  if (row) { isEdit.value = true; Object.assign(form, row) }
  else isEdit.value = false
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) { await updateCategory(form.id, form); ElMessage.success('Updated') }
  else { await createCategory(form); ElMessage.success('Created') }
  dialogVisible.value = false; fetch()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('Confirm delete?', 'Warning', { type: 'warning' })
  await deleteCategory(id); ElMessage.success('Deleted'); fetch()
}

fetch()
</script>
