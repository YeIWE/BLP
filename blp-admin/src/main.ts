import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import ja from 'element-plus/es/locale/lang/ja'
import ko from 'element-plus/es/locale/lang/ko'
import de from 'element-plus/es/locale/lang/de'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import i18n from './locales'
import { useThemeStore } from './stores/theme'
import './assets/styles/themes.scss'

const elLocales: Record<string, any> = { 'zh-CN': zhCn, en, 'ja-JP': ja, 'ko-KR': ko, 'de-DE': de }
const savedLang = localStorage.getItem('lang') || 'zh-CN'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(i18n)
app.use(ElementPlus, { locale: elLocales[savedLang] || zhCn })

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

useThemeStore().initTheme()
app.mount('#app')

export { elLocales }
