<template>
  <el-main>
    <div class="banner-carousel">
      <span class="demonstration"></span>
      <el-carousel height="300px">
        <el-carousel-item
          v-for="(item,i) in bannerImage"
          :key="i"
        >
          <img
            class="banner-img"
            :src=item
          />
        </el-carousel-item>
      </el-carousel>
    </div>
    <div
      v-loading="productLoading"
      class="product-container"
    >
      <el-row
        v-for="(p,i) in product"
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
              :src="o.image.length == 0 ? null : o.image[0].url"
              class="product-image"
            >
            <div class="product-title">
              <h4>{{o.name}}</h4>
              <div class="product-price">
                <h5 class="price-left">{{'¥'+o.price.toFixed(2)}}</h5>
                <h5 class="sale-right">{{'销量: '+o.sale}}</h5>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-pagination
        style="text-align: center"
        background
        :page-size="12"
        layout="prev, pager, next"
        :total="pageSize"
        @current-change="goPage"
        @prev-click="prevPage()"
        @next-click="nextPage()"
      >
      </el-pagination>
    </div>
  </el-main>
</template>

<script>
export default {
  data() {
    return {
      productLoading: false,
      currentPage: 0,
      pageSize: 0,
      product: [],
      bannerImage: [
        require('../assets/banner1.jpg'),
        require('../assets/banner2.jpg'),
        require('../assets/banner3.jpg'),
        require('../assets/banner4.jpg'),
        require('../assets/banner5.jpg'),
        require('../assets/banner6.jpg'),
      ],
    }
  },
  methods: {
    getPageSize() {
      this.$axios
        .get('/product/pageSize')
        .then((res) => {
          if (res.status == 200) {
            this.pageSize = res.data.data
          }
        })
        .catch(() => {})
    },
    getAllProduct() {
      this.productLoading = true
      this.$axios
        .get('/product?page=' + this.currentPage)
        .then((res) => {
          if (res.status == 200) {
            for (let i = 0, len = res.data.data.length; i < len; i += 3) {
              this.product.push(res.data.data.slice(i, i + 3))
            }
            this.productLoading = false
          }
        })
        .catch(() => {
          this.productLoading = false
        })
    },
    goPage(page) {
      this.currentPage = (page - 1)
      this.product = []
      this.getAllProduct()
    },
    prevPage() {
      this.currentPage--
      this.product = []
      this.getAllProduct()
    },
    nextPage() {
      this.currentPage++
      this.product = []
      this.getAllProduct()
    },
    productDetail(product) {
      this.$router.push({ path: '/product', query: { id: product.id } })
    },
  },
  created: function () {
    this.getAllProduct()
    this.getPageSize()
  },
}
</script>

<style>
  .banner-carousel {
    width: 800px;
    margin: 0 auto 20px auto;
  }
  .banner-img {
    height: 300px;
    width: 800px;
    object-fit: cover;
  }
  .product-container {
    width: 90%;
    margin: auto;
  }
  .product-line {
    margin: 10px 0;
  }
  .product-card {
    width: 300px;
    height: 200px;
  }
  .product-image {
    width: 260px;
    height: 120px;
    object-fit: contain;
  }
  .product-title {
    width: 260px;
    margin: 0;
  }
  h4 {
    margin: 0;
  }
  h5 {
    margin: 0;
  }
  .product-title h4 {
    text-align: center;
  }
  .product-price {
    padding: 5px 20px;
  }
  .price-left {
    float: left;
  }
  .sale-right {
    float: right;
  }
</style>
