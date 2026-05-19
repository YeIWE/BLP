<template>
  <el-config-provider :locale="currentElLocale">
    <router-view />
  </el-config-provider>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import ja from 'element-plus/es/locale/lang/ja'
import ko from 'element-plus/es/locale/lang/ko'
import de from 'element-plus/es/locale/lang/de'

const elLocales: Record<string, any> = { 'zh-CN': zhCn, en, 'ja-JP': ja, 'ko-KR': ko, 'de-DE': de }
const currentElLocale = ref(elLocales[localStorage.getItem('lang') || 'zh-CN'] || zhCn)

// Provide a global function to switch locale
;(window as any).__switchElLocale = (lang: string) => {
  currentElLocale.value = elLocales[lang] || zhCn
}
</script>
