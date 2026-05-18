<template>
  <div>
    <h2>{{ $t('dashboard.welcome') }}</h2>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8"><el-card><el-statistic title="Users" :value="1024" /></el-card></el-col>
      <el-col :span="8"><el-card><el-statistic title="Products" :value="256" /></el-card></el-col>
      <el-col :span="8"><el-card><el-statistic title="Orders" :value="5120" /></el-card></el-col>
    </el-row>
  </div>
</template>
