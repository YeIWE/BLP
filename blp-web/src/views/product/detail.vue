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

    <!-- Reviews Section -->
    <div v-if="product" style="margin-top:40px">
      <h3>{{ $t('review.title') }}</h3>
      <el-card style="margin-bottom:16px">
        <div style="display:flex;align-items:center;gap:16px">
          <div>
            <div style="font-size:32px;font-weight:bold;color:var(--primary)">{{ reviewStats.avgRating||0 }}</div>
            <el-rate :model-value="reviewStats.avgRating||0" disabled show-score=false style="margin-top:4px" />
            <div style="color:#999;font-size:13px;margin-top:4px">{{ reviewStats.totalCount||0 }} reviews</div>
          </div>
        </div>
      </el-card>

      <div v-if="reviews.length > 0">
        <el-card v-for="review in reviews" :key="review.id" style="margin-bottom:12px">
          <div style="display:flex;align-items:center;gap:8px;margin-bottom:8px">
            <span style="font-weight:bold">{{ review.userName||review.nickname||'User' }}</span>
            <el-rate :model-value="review.rating" disabled size="small" />
            <span style="color:#999;font-size:12px;margin-left:auto">{{ review.createTime }}</span>
          </div>
          <p style="margin:0;color:#666;line-height:1.6">{{ review.content }}</p>
        </el-card>
        <el-pagination
          style="margin-top:16px;justify-content:center"
          background
          layout="prev,next"
          :total="reviewTotal"
          v-model:current-page="reviewPage"
          @current-change="fetchReviews"
        />
      </div>
      <el-empty v-else :description="$t('review.noReviews')" />
    </div>

    <el-empty v-else :description="$t('web.detail')" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductDetail } from '@/api/product'
import { getReviewPage, getReviewStats } from '@/api/review'

const route = useRoute()
const product = ref<any>(null)
const reviews = ref<any[]>([])
const reviewStats = ref<any>({ avgRating: 0, totalCount: 0 })
const reviewPage = ref(1)
const reviewTotal = ref(0)

async function fetchReviews() {
  try {
    const res:any = await getReviewPage({ productId: Number(route.params.id), page: reviewPage.value, size: 10 })
    reviews.value = res.data?.records||[]
    reviewTotal.value = res.data?.total||0
  } catch {}
}

onMounted(async () => {
  try {
    const pid = Number(route.params.id)
    const res:any = await getProductDetail(pid)
    product.value = res.data
  } catch {}
  try {
    const statsRes:any = await getReviewStats(Number(route.params.id))
    reviewStats.value = statsRes.data||{}
  } catch {}
  fetchReviews()
})
</script>
