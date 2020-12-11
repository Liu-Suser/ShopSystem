<template>
  <el-main>
    <el-page-header
      @back="goBack"
      content="收银台"
      style="margin-bottom:40px"
    >
    </el-page-header>
    <div style="width:80%;margin:auto">
      <el-card style="width:40%;margin: 10px auto">
        <div v-if="address">
          <h5>{{ "收件人: " + address.name }}</h5>
          <h5>{{ "手机: " + address.phone }}</h5>
          <h5>{{ "收件地址: " + address.address }}</h5>
        </div>
        <h4 v-else>
          无地址，请先在用户界面管理地址
          <el-button
            style="margin-left:50px"
            type="primary"
          >用户界面</el-button>
        </h4>
      </el-card>
      <el-table
        :data="checkout"
        show-summary
        :summary-method="getSummary"
      >
        <el-table-column
          label="商品图片"
          width="100"
        >
          <template slot-scope="scope">
            <el-avatar
              shape="square"
              :size="50"
              fit="fill"
              :src="scope.row.product.image == 0 ? null : scope.row.product.image[0].url"
            ></el-avatar>
          </template>
        </el-table-column>
        <el-table-column label="商品名称">
          <template slot-scope="scope">
            <h5>{{ scope.row.product.name }}</h5>
          </template>
        </el-table-column>
        <el-table-column
          label="商品数量"
          width="200"
        >
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.quantity"
              :min="1"
              :max="99"
              size="small"
              label="描述文字"
            ></el-input-number>
          </template>
        </el-table-column>
        <el-table-column
          label="价格"
          width="100"
        >
          <template slot-scope="scope">
            <h5>{{ '¥' + (scope.row.product.price * scope.row.quantity).toFixed(2) }}</h5>
          </template>
        </el-table-column>
        <el-table-column
          label="选择"
          width="80"
        >
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked"></el-checkbox>
          </template>
        </el-table-column>
      </el-table>
      <div class="cart-pop-footer">
        <div style="float:right">
          <el-button @click="goBack()">返 回</el-button>
          <el-button
            type="primary"
            @click="newOrder()"
          >下 单</el-button>
        </div>
      </div>
    </div>
  </el-main>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      addressArray: [],
      address: null
    }
  },
  methods: {
    goBack() {
      history.back()
    },
    getUserAddres() {
      this.$axios
        .get('/user/address')
        .then(res => {
          if (res.status == 200) {
            this.addressArray = res.data.data
            this.address = this.addressArray.filter(function(add) {
              return add.default == true
            })[0]
            if (!this.address) {
              this.address = this.addressArray[0]
            }
          }
        })
        .catch(err => {
          if (err.response.status == 401) {
            this.$message.error('请先登录！')
            this.$router.replace({ path: '/login' })
          } else {
            this.$message.error('操作失败，请稍后重试！')
          }
        })
    },
    newOrder() {
      let orders = {
        aid: this.address.id,
        details: []
      }
      this.checkout.forEach(order => {
        if (order.checked) {
          orders.details.push({
            pid: order.product.id,
            quantity: order.quantity
          })
        }
      })
      this.$axios
        .post('/user/order', orders)
        .then(res => {
          if (res.status == 200) {
            this.$message.success('创建订单成功！跳转到订单页面中...')
            this.$store.commit('removeCheckout')
            setTimeout(() => {
              this.$router.push({ path: '/user' })
            }, 1000)
          }
        })
        .catch(err => {
          if (err.response.status == 401) {
            this.$message.error('请先登录！')
            this.$router.replace({ path: '/login' })
          } else {
            this.$message.error('操作失败，请稍后重试！')
          }
        })
    },
    getSummary(param) {
      const sums = []
      sums[0] = '总价'
      sums[1] = ''
      sums[2] = ''
      sums[3] = 0
      param.data.forEach(item => {
        if (item.checked) {
          sums[3] += item.product.price * item.quantity
        }
      })
      sums[3] = +sums[3].toFixed(2) + '元'
      return sums
    }
  },
  created: function() {
    this.getUserAddres()
  },
  computed: {
    ...mapState({
      checkout: state => state.checkout
    })
  }
}
</script>

<style>
</style>
