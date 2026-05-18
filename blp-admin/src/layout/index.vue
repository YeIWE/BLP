<template>
  <el-container style="height:100vh">
    <el-aside :width="isCollapse?'64px':'220px'" style="background:var(--sidebar-bg);transition:width 0.3s;overflow:hidden">
      <div style="height:60px;display:flex;align-items:center;justify-content:center;color:#fff;font-size:18px;font-weight:bold;border-bottom:1px solid rgba(255,255,255,0.1)">
        <span v-if="!isCollapse">BLP Admin</span>
        <span v-else>BLP</span>
      </div>
      <el-menu :default-active="route.path" :collapse="isCollapse" background-color="transparent" text-color="var(--sidebar-text)" active-text-color="#fff" router>
        <template v-for="item in menuList" :key="item.id">
          <el-sub-menu v-if="item.type===0&&item.children?.length" :index="item.path||String(item.id)">
            <template #title><el-icon><component :is="item.icon" /></el-icon><span>{{ item.name }}</span></template>
            <el-menu-item v-for="child in item.children.filter((c:any)=>c.type===1)" :key="child.id" :index="child.path">{{ child.name }}</el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else-if="item.type===1" :index="item.path">
            <el-icon><component :is="item.icon||'Menu'" /></el-icon><span>{{ item.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="height:60px;display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #e8e8e8;padding:0 20px;background:#fff">
        <div style="display:flex;align-items:center;gap:12px">
          <el-icon style="cursor:pointer;font-size:20px" @click="isCollapse=!isCollapse"><Fold /></el-icon>
          <el-breadcrumb><el-breadcrumb-item :to="{path:'/dashboard'}">{{ $t('menu.home') }}</el-breadcrumb-item><el-breadcrumb-item v-if="route.meta.title">{{ route.meta.title }}</el-breadcrumb-item></el-breadcrumb>
        </div>
        <div style="display:flex;align-items:center;gap:12px">
          <el-dropdown @command="switchLang">
            <span style="cursor:pointer;color:var(--primary)">{{ $t('navbar.lang') }}</span>
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
            <span :style="{display:'inline-block',width:20,height:20,borderRadius:'50%',background:`var(--primary)`,cursor:'pointer',border:themeStore.current===t.key?'3px solid #333':'3px solid transparent',margin:'0 2px'}" @click="themeStore.switchTheme(t.key)" :title="t.key"></span>
          </template>
          <el-dropdown @command="handleCmd">
            <span style="cursor:pointer">Admin &#9660;</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">{{ $t('navbar.logout') }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main style="background:#f5f5f5;padding:20px"><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '@/stores/auth'
import { useThemeStore } from '@/stores/theme'

const route = useRoute()
const { locale } = useI18n()
const authStore = useAuthStore()
const themeStore = useThemeStore()
const isCollapse = ref(false)

const menuList = authStore.menus as any[]

function switchLang(lang: string) { locale.value = lang; localStorage.setItem('lang', lang) }
function handleCmd(cmd: string) { if (cmd === 'logout') authStore.logout() }
</script>
