<template>
  <div class="login-wrapper">
    <div class="login-container">
      <div class="login-left">
        <div class="left-content">
          <h1>BLP 电商管理后台</h1>
          <p>高效 · 智能 · 专业</p>
          <div class="features">
            <div class="feat-item"><span>数据驱动</span> 实时监控业务指标</div>
            <div class="feat-item"><span>精细管理</span> 商品/订单/用户全覆盖</div>
            <div class="feat-item"><span>安全可靠</span> RBAC 权限 + JWT 认证</div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <h2>{{ $t('login.title') }}</h2>
        <el-form @submit.prevent="handleLogin" style="margin-top:32px">
          <el-form-item><el-input v-model="form.username" :placeholder="$t('login.username')" prefix-icon="User" size="large" /></el-form-item>
          <el-form-item><el-input v-model="form.password" type="password" :placeholder="$t('login.password')" prefix-icon="Lock" size="large" show-password @keyup.enter="handleLogin" /></el-form-item>
          <el-form-item><el-button type="primary" size="large" style="width:100%;height:44px" @click="handleLogin" :loading="loading">{{ $t('login.btn') }}</el-button></el-form-item>
        </el-form>
        <div class="login-hint">默认账号: admin / admin123</div>
      </div>
    </div>
    <div class="login-footer">BLP E-Commerce Platform &copy; 2026</div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const form = reactive({ username: 'admin', password: 'admin123' })
const loading = ref(false)

async function handleLogin() {
  if (!form.username || !form.password) { ElMessage.warning('请输入用户名和密码'); return }
  loading.value = true
  try { await authStore.login(form.username, form.password); ElMessage.success('登录成功') }
  catch { ElMessage.error('登录失败，请检查用户名和密码') }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh; background: #f0f2f5; display: flex; align-items: center; justify-content: center;
  flex-direction: column;
}
.login-container {
  width: 800px; background: #fff; border-radius: 12px; overflow: hidden;
  display: flex; box-shadow: 0 8px 40px rgba(0,0,0,0.12);
}
.login-left {
  width: 360px; background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  display: flex; align-items: center; justify-content: center; color: #fff; padding: 48px;
}
.left-content h1 { font-size: 24px; margin: 0 0 8px; }
.left-content p { font-size: 14px; opacity: 0.85; margin: 0 0 32px; }
.features { display: flex; flex-direction: column; gap: 16px; }
.feat-item { font-size: 13px; opacity: 0.9; }
.feat-item span { background: rgba(255,255,255,0.2); padding: 2px 10px; border-radius: 4px; margin-right: 6px; }
.login-right { flex: 1; padding: 48px; }
.login-right h2 { margin: 0; font-size: 22px; color: #333; }
.login-hint { text-align: center; color: #999; font-size: 12px; margin-top: 16px; }
.login-footer { margin-top: 24px; color: #999; font-size: 12px; }
</style>
