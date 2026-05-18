<template>
  <div>
    <!-- Banner -->
    <div style="background:linear-gradient(135deg,var(--primary),var(--primary-dark));color:#fff;padding:60px 24px;text-align:center">
      <h1 style="font-size:36px;margin:0">BLP Mall</h1>
      <p style="font-size:18px;margin:12px 0 24px;opacity:0.9">Discover Amazing Products</p>
      <el-input v-model="keyword" :placeholder="$t('web.searchPlaceholder')" style="max-width:500px" size="large" @keyup.enter="$router.push({path:'/product',query:{keyword}})">
        <template #append><el-button icon="Search" @click="$router.push({path:'/product',query:{keyword}})" /></template>
      </el-input>
    </div>
    <!-- Categories -->
    <div style="max-width:1200px;margin:40px auto;padding:0 24px">
      <h3 style="border-left:4px solid var(--primary);padding-left:12px;margin-bottom:20px">{{ $t('web.categories') }}</h3>
      <el-row :gutter="16">
        <el-col v-for="cat in categories" :key="cat.id" :span="4">
          <el-card shadow="hover" style="text-align:center;cursor:pointer;margin-bottom:16px" @click="$router.push({path:'/product',query:{categoryId:cat.id}})">
            <el-icon :size="32" color="var(--primary)"><component :is="cat.icon||'Folder'" /></el-icon>
            <p style="margin-top:8px">{{ cat.name }}</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <!-- Hot Products -->
    <div style="max-width:1200px;margin:0 auto;padding:0 24px">
      <h3 style="border-left:4px solid var(--primary);padding-left:12px;margin-bottom:20px">{{ $t('web.hotProducts') }}</h3>
      <el-row :gutter="16">
        <el-col v-for="p in products" :key="p.id" :span="6">
          <el-card shadow="hover" style="margin-bottom:16px;cursor:pointer" @click="$router.push(`/product/${p.id}`)">
            <img :src="p.mainImage||'https://placehold.co/300x200?text=Product'" style="width:100%;height:180px;object-fit:cover;border-radius:4px" />
            <p style="font-size:16px;font-weight:bold;margin:8px 0 4px">{{ p.name }}</p>
            <p style="color:var(--primary);font-size:18px;font-weight:bold">￥{{ p.price }}</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getProductPage, getCategoryTree } from '@/api/product'

const keyword = ref('')
const categories = ref<any[]>([])
const products = ref<any[]>([])

onMounted(async () => {
  try { const res:any = await getCategoryTree(); categories.value = (res.data||[]).slice(0, 6) } catch {}
  try { const res:any = await getProductPage({ page:1, size:8 }); products.value = res.data?.records||[] } catch {}
})
</script>
