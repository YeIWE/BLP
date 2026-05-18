<template>
  <div style="max-width:1000px;margin:24px auto;padding:0 24px">
    <el-row :gutter="40" v-if="product">
      <el-col :span="12">
        <img :src="product.mainImage||'https://placehold.co/500x400?text=Product'" style="width:100%;border-radius:8px" />
      </el-col>
      <el-col :span="12">
        <h1 style="font-size:24px;margin:0 0 16px">{{ product.name }}</h1>
        <p style="color:var(--primary);font-size:28px;font-weight:bold;margin:16px 0">
          ￥{{ product.price }}
          <span style="font-size:14px;color:#999;font-weight:normal"> / {{ product.unit||'件' }}</span>
        </p>
        <p style="color:#999;margin:8px 0">{{ $t('web.stock') }}: {{ product.stock }}</p>
        <el-divider />
        <p style="color:#666;line-height:1.8">{{ product.description||'No description yet.' }}</p>
        <div style="margin-top:24px">
          <el-button type="primary" size="large" :disabled="!product.stock">
            {{ $t('web.addToCart') }}
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-empty v-else :description="$t('web.detail')" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductDetail } from '@/api/product'

const route = useRoute()
const product = ref<any>(null)

onMounted(async () => {
  try {
    const res:any = await getProductDetail(Number(route.params.id))
    product.value = res.data
  } catch {}
})
</script>
