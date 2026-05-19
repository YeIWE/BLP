<template>
  <div class="login-wrapper">
    <div class="login-container">
      <div class="login-banner">
        <div class="banner-content">
          <h1>BLP Mall</h1>
          <p>品质生活，从这里开始</p>
          <div class="banner-features">
            <span>正品保障</span><span>极速发货</span><span>无忧售后</span>
          </div>
        </div>
      </div>
      <div class="login-form-panel">
        <el-tabs v-model="activeTab" class="login-tabs">
          <el-tab-pane label="密码登录" name="password">
            <el-form @submit.prevent="handleLogin" style="margin-top:16px">
              <el-form-item><el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" /></el-form-item>
              <el-form-item><el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password @keyup.enter="handleLogin" /></el-form-item>
              <el-form-item><el-button type="primary" size="large" style="width:100%;height:44px" @click="handleLogin" :loading="loading">登 录</el-button></el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="短信登录" name="sms">
            <el-form style="margin-top:16px">
              <el-form-item><el-input v-model="smsForm.phone" placeholder="手机号" prefix-icon="Phone" size="large" /></el-form-item>
              <el-form-item>
                <el-input v-model="smsForm.code" placeholder="验证码" prefix-icon="Message" size="large" style="width:60%" />
                <el-button size="large" style="width:38%;margin-left:2%" @click="sendSms" :disabled="smsCountdown>0">{{ smsCountdown>0 ? smsCountdown+'s' : '获取验证码' }}</el-button>
              </el-form-item>
              <el-form-item><el-button type="primary" size="large" style="width:100%;height:44px" @click="handleSmsLogin">登 录</el-button></el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="扫码登录" name="qr">
            <div style="text-align:center;padding:20px">
              <div style="width:180px;height:180px;background:#f5f5f5;margin:0 auto;display:flex;align-items:center;justify-content:center;border:1px dashed #ddd;border-radius:8px">
                <el-icon :size="48" color="#ccc"><FullScreen /></el-icon>
              </div>
              <p style="color:#999;margin-top:12px;font-size:13px">请使用 BLP App 扫码登录</p>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div class="login-footer">
          <span>测试账号: admin / admin123</span>
          <span style="float:right;color:var(--primary);cursor:pointer" @click="$router.push('/')">返回首页</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const activeTab = ref('password')
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const smsForm = reactive({ phone: '', code: '' })
const smsCountdown = ref(0)

async function handleLogin() {
  if (!form.username || !form.password) { ElMessage.warning('请输入用户名和密码'); return }
  loading.value = true
  try { await authStore.login(form.username, form.password); ElMessage.success('登录成功'); router.push('/') }
  catch { ElMessage.error('登录失败，请检查用户名和密码') }
  finally { loading.value = false }
}

function sendSms() {
  if (!smsForm.phone) { ElMessage.warning('请输入手机号'); return }
  smsCountdown.value = 60
  const timer = setInterval(() => { smsCountdown.value--; if (smsCountdown.value<=0) clearInterval(timer) }, 1000)
  ElMessage.success('验证码已发送（模拟）')
}

function handleSmsLogin() {
  ElMessage.info('短信登录功能开发中，请使用密码登录')
  activeTab.value = 'password'
}
</script>

<style scoped>
.login-wrapper {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex; align-items: center; justify-content: center; padding: 40px 0;
}
.login-container {
  width: 860px; background: #fff; border-radius: 16px; overflow: hidden;
  display: flex; box-shadow: 0 20px 60px rgba(0,0,0,0.3);
}
.login-banner {
  width: 420px; background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  display: flex; align-items: center; justify-content: center; color: #fff; position: relative;
}
.banner-content { text-align: center; }
.banner-content h1 { font-size: 36px; margin: 0 0 8px; }
.banner-content p { font-size: 16px; opacity: 0.9; margin: 0 0 24px; }
.banner-features { display: flex; gap: 16px; justify-content: center; }
.banner-features span { background: rgba(255,255,255,0.2); padding: 4px 16px; border-radius: 20px; font-size: 13px; }
.login-form-panel { flex: 1; padding: 40px 48px; }
.login-tabs :deep(.el-tabs__header) { margin-bottom: 0; }
.login-footer { margin-top: 16px; font-size: 12px; color: #999; }
</style>
