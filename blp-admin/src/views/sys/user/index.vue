<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <el-input v-model="keyword" :placeholder="$t('common.search')" style="width:240px" clearable @clear="fetch" @keyup.enter="fetch" />
        <el-button type="primary" @click="openDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" :label="$t('user.username')" />
        <el-table-column prop="nickname" :label="$t('user.nickname')" />
        <el-table-column prop="email" :label="$t('user.email')" />
        <el-table-column prop="phone" :label="$t('user.phone')" />
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
        <el-form-item :label="$t('user.username')"><el-input v-model="form.username" /></el-form-item>
        <el-form-item :label="$t('user.nickname')"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item :label="$t('user.email')"><el-input v-model="form.email" /></el-form-item>
        <el-form-item :label="$t('user.phone')"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item :label="$t('user.password')"><el-input v-model="form.password" type="password" :placeholder="isEdit?'Leave blank to keep':''" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">{{ $t('common.cancel') }}</el-button><el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserPage, createUser, updateUser, deleteUser } from '@/api/user'

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
  try { const res:any = await getUserPage({ page: page.value, size: 10, keyword: keyword.value }); tableData.value = res.data.records; total.value = res.data.total }
  finally { loading.value = false }
}

function openDialog(row?: any) {
  Object.keys(form).forEach(k => delete form[k])
  if (row) { isEdit.value = true; Object.assign(form, row); form.password = '' }
  else isEdit.value = false
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) { await updateUser(form.id, form); ElMessage.success('Updated') }
  else { await createUser(form); ElMessage.success('Created') }
  dialogVisible.value = false; fetch()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('Confirm delete?', 'Warning', { type: 'warning' })
  await deleteUser(id); ElMessage.success('Deleted'); fetch()
}

fetch()
</script>
