import { createI18n } from 'vue-i18n'
import zhCN from './zh-CN'
import en from './en'
import jaJP from './ja-JP'
import koKR from './ko-KR'
import deDE from './de-DE'

const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('lang') || 'zh-CN',
  fallbackLocale: 'zh-CN',
  messages: { 'zh-CN': zhCN, en, 'ja-JP': jaJP, 'ko-KR': koKR, 'de-DE': deDE },
})

export default i18n
