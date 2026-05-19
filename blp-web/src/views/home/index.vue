<template>
  <div class="home">
    <section class="hero">
      <div class="hero-content">
        <h1>品质生活，从这里开始</h1>
        <p>精选好物 · 正品保障 · 极速发货</p>
        <div class="hero-search">
          <el-input v-model="keyword" placeholder="搜索商品..." size="large" @keyup.enter="goSearch" class="search-input">
            <template #append><el-button icon="Search" @click="goSearch">搜索</el-button></template>
          </el-input>
        </div>
        <div class="hero-tags"><el-tag v-for="t in ['手机','电脑','服饰','美妆','食品']" :key="t" @click="keyword=t;goSearch()" style="cursor:pointer;margin:0 4px">{{ t }}</el-tag></div>
      </div>
    </section>
    <section class="section">
      <div class="section-header"><h2>商品分类</h2><span class="section-more" @click="$router.push('/product')">查看全部 →</span></div>
      <el-row :gutter="16"><el-col :span="4" v-for="cat in categories" :key="cat.id"><div class="cat-card" @click="$router.push({path:'/product',query:{categoryId:cat.id}})"><el-icon :size="36" color="var(--primary)"><component :is="cat.icon||'Folder'" /></el-icon><p>{{ cat.name }}</p></div></el-col></el-row>
    </section>
    <section class="section">
      <div class="section-header"><h2>热门推荐</h2><span class="section-more" @click="$router.push('/product')">更多 →</span></div>
      <el-row :gutter="16"><el-col :span="6" v-for="p in products" :key="p.id"><el-card shadow="hover" class="product-card" @click="$router.push('/product/'+p.id)"><div class="product-img"><img :src="p.mainImage||'https://placehold.co/300x200/f5f5f5/999?text=商品'" :alt="p.name" /><div class="product-tag" v-if="p.id%3===0">热卖</div></div><div class="product-info"><p class="product-name">{{ p.name }}</p><p class="product-price"><span class="price-symbol">¥</span>{{ p.price }}</p></div></el-card></el-col></el-row>
    </section>
    <footer class="home-footer"><div class="footer-grid"><div><h4>购物指南</h4><p>购物流程</p><p>支付方式</p></div><div><h4>售后服务</h4><p>退换货政策</p><p>客服中心</p></div><div><h4>关于我们</h4><p>公司介绍</p><p>联系我们</p></div><div><h4>客服</h4><p style="font-size:18px;color:var(--primary)">400-888-8888</p><p>工作日 9:00-18:00</p></div></div><div class="footer-bottom">BLP Mall &copy; 2026</div></footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductPage, getCategoryTree } from '@/api/product'
const router = useRouter()
const keyword = ref('')
const categories = ref<any[]>([])
const products = ref<any[]>([])
function goSearch() { router.push({ path: '/product', query: { keyword: keyword.value } }) }
onMounted(async () => {
  try { const r:any = await getCategoryTree(); categories.value = (r.data||[]).slice(0,6) } catch {}
  try { const r:any = await getProductPage({ page:1, size:8 }); products.value = r.data?.records||[] } catch {}
})
</script>

<style scoped>
.home{background:#f5f5f5}.hero{background:linear-gradient(135deg,#667eea,#764ba2);color:#fff;text-align:center;padding:60px 24px}.hero-content h1{font-size:36px;margin:0 0 8px}.hero-content p{font-size:16px;opacity:.9;margin:0 0 24px}.hero-search{max-width:560px;margin:0 auto 16px}.hero-tags{margin-top:8px}.section{max-width:1200px;margin:0 auto;padding:40px 24px}.section-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:20px}.section-header h2{font-size:20px;color:#333;margin:0;padding-left:12px;border-left:4px solid var(--primary)}.section-more{color:#999;cursor:pointer;font-size:14px}.section-more:hover{color:var(--primary)}.cat-card{text-align:center;padding:24px 12px;background:#fff;border-radius:8px;cursor:pointer;transition:.2s}.cat-card:hover{box-shadow:0 4px 12px rgba(0,0,0,.1);transform:translateY(-2px)}.cat-card p{margin:8px 0 0;font-size:14px;color:#333}.product-card{border-radius:8px;overflow:hidden;cursor:pointer}.product-img{position:relative;overflow:hidden}.product-img img{width:100%;height:180px;object-fit:cover;transition:transform .3s}.product-card:hover .product-img img{transform:scale(1.05)}.product-tag{position:absolute;top:8px;left:8px;background:var(--primary);color:#fff;padding:2px 8px;border-radius:4px;font-size:12px}.product-info{padding:12px}.product-name{font-size:14px;color:#333;margin:0 0 8px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.product-price{font-size:18px;color:#f56c6c;font-weight:700;margin:0}.price-symbol{font-size:12px;margin-right:2px}.home-footer{background:#2c2c2c;color:#999;margin-top:40px}.footer-grid{max-width:1200px;margin:0 auto;padding:40px 24px;display:grid;grid-template-columns:repeat(4,1fr);gap:24px}.footer-grid h4{color:#fff;font-size:14px;margin:0 0 12px}.footer-grid p{font-size:12px;margin:4px 0;cursor:pointer}.footer-grid p:hover{color:var(--primary)}.footer-bottom{text-align:center;padding:16px;border-top:1px solid #444;font-size:12px}
</style>
