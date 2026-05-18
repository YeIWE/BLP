<template>
  <div style="max-width:1200px;margin:24px auto;padding:0 24px">
    <el-row :gutter="20">
      <el-col :span="5">
        <el-card>
          <h4>{{ $t('web.categories') }}</h4>
          <el-menu style="border:none">
            <el-menu-item v-for="cat in categories" :key="cat.id" :index="String(cat.id)" @click="categoryId=cat.id;fetch()">{{ cat.name }}</el-menu-item>
            <el-menu-item index="" @click="categoryId=undefined;fetch()">{{ $t('web.all') }}</el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
      <el-col :span="19">
        <el-input v-model="keyword" :placeholder="$t('web.searchPlaceholder')" style="margin-bottom:16px" @keyup.enter="fetch" clearable @clear="fetch" />
        <el-row :gutter="16">
          <el-col v-for="p in products" :key="p.id" :span="8">
            <el-card shadow="hover" style="margin-bottom:16px;cursor:pointer" @click="$router.push(`/product/${p.id}`)">
              <img :src="p.mainImage||'https://placehold.co/300x200?text=Product'" style="width:100%;height:160px;object-fit:cover;border-radius:4px" />
              <p style="font-weight:bold;margin:8px 0 4px">{{ p.name }}</p>
              <p style="color:#999;font-size:12px">{{ p.stock }} in stock</p>
              <p style="color:var(--primary);font-size:18px;font-weight:bold">￥{{ p.price }}</p>
            </el-card>
          </el-col>
        </el-row>
        <el-pagination style="margin-top:16px;justify-content:center" background layout="prev,next" :total="total" v-model:current-page="page" @current-change="fetch" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductPage, getCategoryTree } from '@/api/product'

const route = useRoute()
const products = ref<any[]>([])
const categories = ref<any[]>([])
const page = ref(1)
const total = ref(0)
const keyword = ref(route.query.keyword as string || '')
const categoryId = ref(route.query.categoryId?Number(route.query.categoryId):undefined)

async function fetch() {
  try {
    const res:any = await getProductPage({ page: page.value, size: 12, keyword: keyword.value, categoryId: categoryId.value })
    products.value = res.data?.records||[]; total.value = res.data?.total||0
  } catch {}
}

onMounted(async () => {
  try { const res:any = await getCategoryTree(); categories.value = res.data||[] } catch {}
  fetch()
})
</script>
