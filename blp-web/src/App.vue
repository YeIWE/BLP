<template>
  <div>
    <header style="background:var(--primary);color:#fff;padding:0 24px;display:flex;align-items:center;justify-content:space-between;height:60px">
      <div style="display:flex;align-items:center;gap:24px;font-size:16px">
        <router-link to="/" style="color:#fff;text-decoration:none;font-size:20px;font-weight:bold">BLP Mall</router-link>
        <router-link to="/" style="color:#fff;text-decoration:none">{{ $t('web.home') }}</router-link>
        <router-link to="/product" style="color:#fff;text-decoration:none">{{ $t('web.search') }}</router-link>
        <router-link to="/cart" style="color:#fff;text-decoration:none">{{ $t('cart.title') }}</router-link>
        <router-link to="/orders" style="color:#fff;text-decoration:none">{{ $t('orders.title') }}</router-link>
        <router-link to="/seckill" style="color:#fff;text-decoration:none">{{ $t('seckill.title') }}</router-link>
      </div>
      <div style="display:flex;align-items:center;gap:12px">
        <el-dropdown @command="switchLang">
          <span style="cursor:pointer;color:#fff">{{ $t('navbar.lang') }}</span>
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
          <span :style="{display:'inline-block',width:18,height:18,borderRadius:'50%',cursor:'pointer',border:themeStore.current===t.key?'2px solid #fff':'2px solid transparent'}" @click="themeStore.switchTheme(t.key)"></span>
        </template>
      </div>
    </header>
    <main><router-view /></main>
    <footer style="text-align:center;padding:24px;color:#999;border-top:1px solid #eee;margin-top:40px">
      BLP Mall &copy; 2026 - Built with Vue 3 + Element Plus
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useThemeStore } from '@/stores/theme'
import { onMounted } from 'vue'

const { locale } = useI18n()
const themeStore = useThemeStore()

onMounted(() => themeStore.initTheme())

function switchLang(lang: string) { locale.value = lang; localStorage.setItem('lang', lang) }
</script>
