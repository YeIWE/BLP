<template>
  <el-config-provider :locale="currentElLocale">
  <div>
    <header style="background:var(--primary);color:#fff;padding:0 24px;display:flex;align-items:center;justify-content:space-between;height:60px">
      <div style="display:flex;align-items:center;gap:24px;font-size:16px">
        <router-link to="/" style="color:#fff;text-decoration:none;font-size:20px;font-weight:bold">BLP Mall</router-link>
        <router-link to="/product" style="color:#fff;text-decoration:none">搜索</router-link>
        <router-link to="/cart" style="color:#fff;text-decoration:none">购物车</router-link>
        <router-link to="/orders" style="color:#fff;text-decoration:none">订单</router-link>
        <router-link to="/seckill" style="color:#fff;text-decoration:none">秒杀</router-link>
      </div>
      <div style="display:flex;align-items:center;gap:12px">
        <template v-if="authStore.token">
          <span style="color:#fff">{{ authStore.userInfo?.username || '用户' }}</span>
          <span style="color:#fff;cursor:pointer" @click="authStore.logout()">退出</span>
        </template>
        <router-link v-else to="/login" style="color:#fff;text-decoration:none">登录</router-link>
        <el-dropdown @command="switchLang">
          <span style="cursor:pointer;color:#fff">语言</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="zh-CN">中文</el-dropdown-item>
              <el-dropdown-item command="en">English</el-dropdown-item>
              <el-dropdown-item command="ja-JP">日本語</el-dropdown-item>
              <el-dropdown-item command="ko-KR">한국어</el-dropdown-item>
              <el-dropdown-item command="de-DE">Deutsch</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <template v-for="t in themeStore.themes" :key="t.key">
          <span :style="{display:'inline-block',width:18,height:18,borderRadius:'50%',background:themeColors[t.key],cursor:'pointer',border:themeStore.current===t.key?'2px solid #fff':'2px solid transparent'}" @click="themeStore.switchTheme(t.key)" :title="t.key"></span>
        </template>
      </div>
    </header>
    <main><router-view /></main>
    <footer style="text-align:center;padding:24px;color:#999;border-top:1px solid #eee;margin-top:40px">
      BLP Mall &copy; 2026 - Vue 3 + Element Plus
    </footer>
  </div>
  </el-config-provider>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useThemeStore } from '@/stores/theme'
import { useAuthStore } from '@/stores/auth'
import { onMounted } from 'vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import ja from 'element-plus/es/locale/lang/ja'
import ko from 'element-plus/es/locale/lang/ko'
import de from 'element-plus/es/locale/lang/de'

const elLocales: Record<string, any> = { 'zh-CN': zhCn, en, 'ja-JP': ja, 'ko-KR': ko, 'de-DE': de }
const currentElLocale = ref(elLocales[localStorage.getItem('lang') || 'zh-CN'] || zhCn)

const { locale } = useI18n()
const themeStore = useThemeStore()
const authStore = useAuthStore()
const themeColors: Record<string, string> = { blue: '#1890FF', yellow: '#F5A623', pink: '#EB2F96', green: '#52C41A', purple: '#722ED1' }

onMounted(() => themeStore.initTheme())

function switchLang(lang: string) {
  locale.value = lang
  localStorage.setItem('lang', lang)
  currentElLocale.value = elLocales[lang] || zhCn
}
</script>
