<template>
  <div style="display:flex;justify-content:center;align-items:center;height:100vh;background:linear-gradient(135deg,var(--primary-light),#f0f2f5)">
    <el-card style="width:400px">
      <h2 style="text-align:center;color:var(--primary);margin-bottom:24px">{{ $t('login.title') }}</h2>
      <el-form @submit.prevent="handleLogin">
        <el-form-item><el-input v-model="form.username" :placeholder="$t('login.username')" prefix-icon="User" size="large" /></el-form-item>
        <el-form-item><el-input v-model="form.password" type="password" :placeholder="$t('login.password')" prefix-icon="Lock" size="large" show-password @keyup.enter="handleLogin" /></el-form-item>
        <el-form-item><el-button type="primary" size="large" style="width:100%" @click="handleLogin" :loading="loading">{{ $t('login.btn') }}</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const form = reactive({ username: 'admin', password: 'admin123' })
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await authStore.login(form.username, form.password)
    ElMessage.success('登录成功')
  } catch {
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}
</script>
