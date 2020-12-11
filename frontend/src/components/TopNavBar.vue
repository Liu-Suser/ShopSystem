<template>
  <div class="container">
    <div class="homeicon">
      <img
        class="icon"
        src="../assets/logo.png"
      />
    </div>
    <ul class="top-nav">
      <li>
        <el-input
          v-model="name"
          class="nav-search"
          placeholder="请输入商品名称"
          clearable
        >
          <el-button
            slot="append"
            size="small"
            icon="el-icon-search"
            @click="goSearch()"
          ></el-button>
        </el-input>
      </li>
      <li>
        <el-link
          :underline="false"
          href="/"
        >主页</el-link>
      </li>
      <li v-show="[0,1,2].indexOf(role) == -1">
        <el-popover
          v-model="cartPop"
          placement="bottom"
          width="400"
        >
          <el-table
            :data="cartArray"
            style="width: 100%"
          >
            <el-table-column
              label="商品图片"
              width="80"
            >
              <template slot-scope="scope">
                <el-avatar
                  shape="square"
                  :size="50"
                  fit="fill"
                  :src="scope.row.productId.image.length == 0 ? null : scope.row.productId.image[0].url"
                ></el-avatar>
              </template>
            </el-table-column>
            <el-table-column
              label="商品名称"
              width="150"
            >
              <template slot-scope="scope">
                <h5>{{ scope.row.productId.name }}</h5>
              </template>
            </el-table-column>
            <el-table-column
              label="商品数量"
              width="80"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.quantity }}</span>
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
            <el-button
              style="float:left"
              size="small"
              type="danger"
              @click="deleteCart()"
            >删 除</el-button>
            <div style="float:right">
              <el-button
                size="small"
                @click="cartPop = false"
              >取 消</el-button>
              <el-button
                size="small"
                type="primary"
                @click="cartPop = false;goCheckout()"
              >结 算</el-button>
            </div>
          </div>
          <el-link
            slot="reference"
            :underline="false"
            @click="getCart()"
          >购物车</el-link>
        </el-popover>
      </li>
      <li v-show="[0,1,2].indexOf(role) == -1">
        <el-link
          v-if="token"
          :underline="false"
          href="/user"
        >我的</el-link>
      </li>
      <li v-show="[0,1,2].indexOf(role) == -1">
        <el-link
          v-if="!token"
          :underline="false"
          href="/login"
        >登录</el-link>
      </li>
      <li v-show="[0,1,2].indexOf(role) > -1">
        <el-link
          :underline="false"
          href="/dashboard"
        >后台</el-link>
      </li>
      <li v-show="[0,1,2].indexOf(role) > -1">
        <el-link
          :underline="false"
          @click="logout"
        >退出登录</el-link>
      </li>
    </ul>

  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      cartPop: false,
      cartArray: [],
      name: ''
    }
  },
  methods: {
    goSearch() {
      this.$router.push({ path: '/search', query: { name: this.name } })
      if (this.$router.currentRoute.path == '/search') {
        this.$router.go(0)
      }
    },
    getCart() {
      this.$axios
        .get('/user/cart')
        .then(res => {
          if (res.status == 200) {
            this.cartArray = res.data.data
          }
        })
        .catch(err => {
          if (err.response.status == 401) {
            this.$message.error('请先登录后再操作！')
          } else {
            this.$message.error('获取购物车，请稍后重试！')
          }
        })
    },
    deleteCart() {
      const params = new URLSearchParams()
      this.cartArray.forEach(cart => {
        if (cart.checked) {
          params.append('isDelete', cart.checked)
          this.$axios
            .put('/user/cart/' + cart.id, params)
            .then(res => {
              if (res.status == 200) {
                this.getCart()
                this.$message.success(res.data.msg)
              }
            })
            .catch(() => {
              this.$message.error('操作失败，请稍后重试！')
            })
        }
      })
    },
    goCheckout() {
      const checkout = []
      this.cartArray.forEach(cart => {
        if (cart.checked) {
          checkout.push({
            product: cart.productId,
            addressId: '',
            quantity: cart.quantity,
            checked: true
          })
        }
      })
      this.$store.commit('updateCheckout', checkout)
      if (this.$router.currentRoute.path != '/checkout') {
        this.$router.push({ path: '/checkout' })
      }
    },
    logout() {
      this.$message.success('退出登录成功！跳转主页中...')
      this.$store.commit('removeToken')
      this.$store.commit('removeRole')
      this.$store.commit('removeCheckout')
      setTimeout(() => {
        this.$router.push({ path: '/' })
      }, 1000)
    }
  },
  created: function() {},
  computed: {
    ...mapState({
      token: state => state.token,
      role: state => parseInt(state.role)
    })
  }
}
</script>

<style>
  .el-header {
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-menu {
    border: none !important;
  }
  .nav-search {
    width: 200px;
  }
  .container {
    height: 65px;
    padding: 0 100px;
    border-bottom: 1px solid #dcdfe6;
    display: block;
  }
  .homeicon {
    float: left;
    height: 65px;
  }
  .icon {
    height: 40px;
    width: 40px;
    margin-top: 8px;
  }
  .top-nav {
    float: right;
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .top-nav li {
    float: left;
    margin: 0 20px;
  }
  .cart-pop-footer {
    padding: 10px;
  }
</style>
