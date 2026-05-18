import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const themes = [
    { key: 'blue', label: 'theme.blue' },
    { key: 'yellow', label: 'theme.yellow' },
    { key: 'pink', label: 'theme.pink' },
    { key: 'green', label: 'theme.green' },
    { key: 'purple', label: 'theme.purple' },
  ]
  const current = ref(localStorage.getItem('theme') || 'blue')

  function switchTheme(theme: string) {
    current.value = theme
    localStorage.setItem('theme', theme)
    document.documentElement.setAttribute('data-theme', theme)
  }

  function initTheme() {
    document.documentElement.setAttribute('data-theme', current.value)
  }

  return { themes, current, switchTheme, initTheme }
})
