<template>
  <el-main>
    <el-page-header
      @back="goBack"
      :content="product.name"
      style="margin-bottom:40px"
    >
    </el-page-header>
    <div>
      <el-carousel class="product-img-carousel">
        <el-carousel-item
          v-for="(img,i) in product.image"
          :key="i"
        >
          <img
            class="product-img"
            :src="img.url"
          />
        </el-carousel-item>
      </el-carousel>
      <div class="product-price">
        <h3>{{product.subtitle}}</h3>
        <span style="font-size:30px;font-weight: bold;">{{'¥'+product.price.toFixed(2)}}</span>
        <span style="margin-left:100px;font-weight: bold;">{{'销量: '+product.sale}}</span>

      </div>
      <div class="product-checkout">
        <el-input-number
          :disabled="product.stock == 0"
          v-model="quantity"
          :min="1"
          :max="10"
          label="数量"
        ></el-input-number>
        <div style="margin-top:20px">
          <el-button
            :disabled="[0,1,2].indexOf(role) > -1"
            type="primary"
            @click="addCart()"
          >加入购物车</el-button>
          <el-button
            :disabled="[0,1,2].indexOf(role) > -1 || product.stock == 0"
            type="primary"
            @click="goCheckout()"
          >立即购买</el-button>
        </div>
      </div>
    </div>
    <el-tabs
      value="detail"
      class="product-detail"
      stretch
    >
      <el-tab-pane
        label="商品详情"
        name="detail"
      >
        <h5 style="height:400px">{{product.detail}}</h5>
      </el-tab-pane>
      <el-tab-pane
        label="商品评价"
        name="comment"
      >
        <h3
          v-if="commentArray && commentArray.length == 0"
          style="height:200px;text-align: center"
        >暂无评论</h3>
        <div v-else>
          <el-row
            v-for="(c,i) in commentArray"
            :key="i"
          >
            <el-col
              :span="8"
              v-for="(d,index) in c"
              :key="index"
            >
              <el-card
                shadow="hover"
                style="height:200px;margin: 10px"
              >
                <div class="user-icon">
                  <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
                  <h4>{{'用户 '+d.userId.nickname}}</h4>
                </div>
                <div style="margin:20px">
                  <el-rate
                    :value="d.rate"
                    disabled
                  ></el-rate>
                  <div style="margin-top:10px">
                    <h5>
                      {{ d.comment }}
                    </h5>
                    <h5 style="float:right">
                      {{ new Date(d.createTime) | date-format }}
                    </h5>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-main>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      quantity: 1,
      product: {
        price: 0,
      },
      commentArray: [],
    }
  },
  methods: {
    goBack() {
      history.back()
    },
    getProduct() {
      this.$axios
        .get('/product/' + this.$route.query.id)
        .then((res) => {
          this.product = res.data
        })
        .catch(() => {
          this.$message.error('加载失败，请稍后重试！')
        })
    },
    addCart() {
      if (this.token) {
        const params = new URLSearchParams()
        params.append('productId', this.product.id)
        params.append('quantity', this.quantity)
        params.append('checked', true)
        this.$axios
          .post('/user/cart', params)
          .then((res) => {
            if (res.status == 200) {
              this.$message.success(res.data.msg)
            }
          })
          .catch((err) => {
            if (err.response.status == 401) {
              this.$message.error('请先登录后再操作！')
            } else {
              this.$message.error('操作失败，请稍后重试！')
            }
          })
      } else {
        this.$message.error('请先登录后再操作！')
      }
    },
    getComment() {
      this.$axios
        .get('/product/' + this.$route.query.id + '/comment')
        .then((res) => {
          for (let i = 0, len = res.data.length; i < len; i += 3) {
            this.commentArray.push(res.data.slice(i, i + 3))
          }
        })
        .catch(() => {
          this.$message.error('加载评论失败，请稍后重试！')
        })
    },
    goCheckout() {
      if (this.token) {
        const checkout = [
          {
            product: this.product,
            addressId: '',
            quantity: this.quantity,
            checked: true,
          },
        ]
        this.$store.commit('updateCheckout', checkout)
        this.$router.push({ path: '/checkout' })
      } else {
        this.$message.error('请先登录后再购买！')
      }
    },
  },
  created: function () {
    this.getProduct()
    this.getComment()
  },
  computed: {
    ...mapState({
      token: (state) => state.token,
      role: (state) => parseInt(state.role)
    }),
  },
}
</script>

<style>
  .product-container {
    width: 90%;
    margin: auto;
  }
  .product-img-carousel {
    width: 500px;
    margin: 0 50px 20px 0;
    float: left;
  }
  .product-img {
    height: 300px;
    width: 500px;
    object-fit: contain;
  }
  .product-price {
    height: 230px;
  }
  .product-detail {
    margin-top: 50px;
  }
</style>
