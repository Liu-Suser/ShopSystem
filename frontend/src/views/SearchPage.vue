<template>
  <el-main class="main-container">
    <el-page-header
      @back="goBack"
      content="搜索结果"
      style="margin-bottom:40px"
    >
    </el-page-header>
    <div
      v-loading="loading"
      class="product-container"
    >
      <h3
        v-if="loading == false && productArray.length == 0"
        style="text-align: center"
      >暂无数据</h3>
      <el-row
        v-else
        v-for="(p,i) in productArray"
        :key="i"
        class="product-line"
      >
        <el-col
          :span="8"
          v-for="(o, index) in p"
          :key="index"
        >
          <el-card
            shadow="hover"
            class="product-card"
            @click.native="productDetail(o)"
          >
            <img
              :src="o.image[0].url"
              class="product-image"
            >
            <div class="product-title">
              <h4>{{o.name}}</h4>
              <div class="product-price">
                <h5 class="price-left">{{'¥'+o.price}}</h5>
                <h5 class="price-right">{{'销量: '+o.sale}}</h5>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </el-main>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      productArray: [],
    }
  },
  methods: {
    goBack() {
      history.back()
    },
    searchProduct() {
      this.$axios
        .get('/product/search?product=' + this.$route.query.name)
        .then((res) => {
          if (res.status == 200) {
            for (let i = 0, len = res.data.data.length; i < len; i += 3) {
              this.productArray.push(res.data.data.slice(i, i + 3))
            }
            this.loading = false
          }
        })
        .catch(() => {
          this.loading = false
          this.$message.error('加载失败，请稍后重试！')
        })
    },
    productDetail(product) {
      this.$router.push({ path: '/product', query: { id: product.id } })
    },
  },
  created: function () {
    this.searchProduct()
  },
  computed: {},
}
</script>

<style>
  .product-container {
    width: 90%;
    margin: auto;
  }
  .main-container {
    margin: 0 40px;
  }
</style>