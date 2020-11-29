<template>
  <div id="app">
    <el-container>
      <el-header>
        <TopNavBar />
      </el-header>
      <el-container>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script>
import TopNavBar from '@/components/TopNavBar.vue'
export default {
  components: {
    TopNavBar
  }
}
</script>
