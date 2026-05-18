<template>
  <div>
    <el-card>
      <div style="display:flex;justify-content:space-between;margin-bottom:16px">
        <span />
        <el-button type="primary" @click="openDialog()">{{ $t('common.create') }}</el-button>
      </div>
      <el-table :data="tableData" border row-key="id" v-loading="loading" :tree-props="{children:'children'}">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" :label="$t('menuMgr.name')" />
        <el-table-column prop="path" :label="$t('menuMgr.path')" />
        <el-table-column prop="icon" :label="$t('menuMgr.icon')" />
        <el-table-column prop="permission" :label="$t('menuMgr.permission')" />
        <el-table-column :label="$t('menuMgr.type')" width="100">
          <template #default="{row}">
            <el-tag v-if="row.type===0">{{ $t('menuMgr.dir') }}</el-tag>
            <el-tag v-else-if="row.type===1" type="success">{{ $t('menuMgr.menu') }}</el-tag>
            <el-tag v-else type="warning">{{ $t('menuMgr.button') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('menuMgr.sort')" width="80" prop="sort" />
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
        <el-form-item :label="$t('menuMgr.name')"><el-input v-model="form.name" /></el-form-item>
        <el-form-item :label="$t('menuMgr.path')"><el-input v-model="form.path" /></el-form-item>
        <el-form-item :label="$t('menuMgr.component')"><el-input v-model="form.component" /></el-form-item>
        <el-form-item :label="$t('menuMgr.icon')"><el-input v-model="form.icon" /></el-form-item>
        <el-form-item :label="$t('menuMgr.type')">
          <el-select v-model="form.type" style="width:100%">
            <el-option :value="0" :label="$t('menuMgr.dir')" />
            <el-option :value="1" :label="$t('menuMgr.menu')" />
            <el-option :value="2" :label="$t('menuMgr.button')" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('menuMgr.permission')"><el-input v-model="form.permission" /></el-form-item>
        <el-form-item :label="$t('menuMgr.sort')"><el-input-number v-model="form.sort" /></el-form-item>
        <el-form-item :label="$t('category.parentId')">
          <el-tree-select v-model="form.parentId" :data="tableData" :props="{label:'name',value:'id',children:'children'}" check-strictly clearable :placeholder="$t('category.root')" style="width:100%" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">{{ $t('common.cancel') }}</el-button><el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuTree, createMenu, updateMenu, deleteMenu } from '@/api/user'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive<any>({ type: 0, sort: 0 })

async function fetch() {
  loading.value = true
  try { const res: any = await getMenuTree(); tableData.value = res.data || [] }
  finally { loading.value = false }
}

function openDialog(row?: any) {
  Object.keys(form).forEach(k => delete form[k])
  if (row) { isEdit.value = true; Object.assign(form, row) }
  else { isEdit.value = false; form.type = 0; form.sort = 0 }
  dialogVisible.value = true
}

async function handleSave() {
  if (isEdit.value) { await updateMenu(form.id, form); ElMessage.success('Updated') }
  else { await createMenu(form); ElMessage.success('Created') }
  dialogVisible.value = false; fetch()
}

async function handleDelete(id: number) {
  await ElMessageBox.confirm('Confirm delete?', 'Warning', { type: 'warning' })
  await deleteMenu(id); ElMessage.success('Deleted'); fetch()
}

fetch()
</script>
