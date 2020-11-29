<template>
  <el-container>
    <SideNavBar />
    <MainPage />
  </el-container>

</template>

<script>
import SideNavBar from '@/components/SideNavBar.vue'
import MainPage from '@/views/MainPage.vue'
export default {
  components: {
    SideNavBar,
    MainPage,
  },
}
</script>
