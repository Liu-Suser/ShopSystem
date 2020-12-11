<template>
  <el-main>
    <div
      v-if="[0,1].indexOf(role) > -1"
      class="table-search-user"
    >
      <el-input
        v-model="searchUser"
        placeholder="搜索用户名"
      >
      </el-input>
      <el-button-group style="margin-top:-1px">
        <el-button
          icon="el-icon-search"
          @click="searchUserOrder()"
        ></el-button>
        <el-button
          icon="el-icon-circle-close"
          @click="clearSearchUserOrder()"
        ></el-button>
      </el-button-group>
    </div>
    <el-table
      v-loading="orderLoading"
      :data="orderArray"
      lazy
      style="width: 100%"
      :default-sort="{ prop: 'date', order: null }"
    >
      <el-table-column
        label="订单号"
        width="80"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="[0,1,2].indexOf(role) > -1"
        label="用户名"
        width="80"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.user.username }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="商品信息"
        width="220"
      >
        <template slot-scope="scope">
          <el-row
            v-for="(detail,i) in scope.row.details"
            :key="i"
          >
            <el-col :span="10">
              <el-image
                style="width: 70px; height: 70px"
                :src="detail.product.image.length == 0 ? null : detail.product.image[0].url"
                fit="cover"
              ></el-image>
            </el-col>
            <el-col
              :span="14"
              style="font-size:10px"
            >
              <span>{{ detail.product.name }}</span>
              <p style="margin:0"> 数量<el-tag size="small">{{ detail.quantity }}</el-tag>
              </p>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column
        prop="date"
        label="总金额"
        width="80"
      >
        <template slot-scope="scope">
          <h5 style="color:#67C23A">{{'¥' + scope.row.price.toFixed(2)}}</h5>
        </template>
      </el-table-column>
      <el-table-column
        label="收货人信息"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.address.name }}</el-tag>
          <el-tag size="medium">{{ scope.row.address.phone }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="收货地址"
        width="120"
      >
        <template slot-scope="scope">
          <h5>{{ scope.row.address.address }}</h5>
        </template>
      </el-table-column>
      <el-table-column
        prop="date"
        label="下单时间"
        width="100"
        sortable
      >
        <template slot-scope="scope">
          <h5>{{ new Date(scope.row.createTime) | dateFormat }}</h5>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="订单状态"
        width="110"
        :filters="filterOrder"
        :filter-method="filterOrderStatus"
      >
        <template slot-scope="scope">
          <h5 v-if="scope.row.status == 'Payed'">
            {{ orderStatus[scope.row.status]}}
            <h5 style="color:#67C23A">{{'¥' + scope.row.price.toFixed(2)}}</h5>
          </h5>
          <h5 v-else-if="['Shipped', 'Transit'].indexOf(scope.row.status) > -1">
            <h5>{{ orderStatus[scope.row.status] }}</h5>
            <el-tag size="small">{{ scope.row.express }}</el-tag>
            <el-tag size="small">{{ scope.row.logistics }}</el-tag>
          </h5>
          <h5 v-else>{{ orderStatus[scope.row.status]}}</h5>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="[0,1].indexOf(role) > -1"
            size="mini"
            type="primary"
            @click="manageDialog = !manageDialog; manageFrom = scope.row"
          >编辑</el-button>
          <el-popover
            v-if="role == 2 && ['Payed','Shipped','Transit'].indexOf(scope.row.status) > -1"
            placement="bottom"
            width="200"
            :ref="`popover-${scope.$index}`"
          >
            <div>
              <el-form :model="expressFrom">
                <el-form-item
                  label="快递公司"
                  label-width="120"
                >
                  <el-input
                    v-model="expressFrom.express"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item
                  label="快递单号"
                  label-width="120"
                >
                  <el-input
                    v-model="expressFrom.logistics"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div style="text-align: center; margin: auto">
                <el-button
                  size="mini"
                  @click="scope._self.$refs[`popover-${scope.$index}`].doClose();expressFrom = {}"
                >取 消</el-button>
                <el-button
                  size="mini"
                  type="primary"
                  @click="scope._self.$refs[`popover-${scope.$index}`].doClose();shippeOrder(scope.row)"
                >确 认</el-button>
              </div>
            </div>
            <el-button
              size="mini"
              type="primary"
              slot="reference"
            >{{scope.row.status == 'Payed' ? "发货":"编辑物流"}}</el-button>
          </el-popover>
          <el-popover
            v-if="role == 3 && scope.row.status == 'Ordered'"
            placement="bottom"
            width="200"
          >
            <div style="text-align: center; margin: auto">
              <h4 style="color:#67C23A">{{'¥' + scope.row.price.toFixed(2)}}</h4>
              <img
                width="200px"
                src="../assets/pay.png"
                alt=""
              >
              <div>
                <el-button
                  size="mini"
                  @click="payOrder(scope.row)"
                >已支付</el-button>
              </div>
            </div>
            <el-button
              style="margin-right:10px"
              size="mini"
              slot="reference"
            >支付</el-button>
          </el-popover>
          <el-popover
            v-show="role == 3 && scope.row.status == 'Completed' && filterNotComment(scope.row.details).length > 0"
            placement="bottom"
            width="190"
            :ref="`popover-${scope.$index}`"
          >

            <el-form :model="comment">
              <el-form-item
                label="评价"
                label-width="90"
              >
                <el-select
                  v-model="comment.detailId"
                  placeholder="请选择商品"
                >
                  <el-option
                    v-for="detail in filterNotComment(scope.row.details)"
                    :key="detail.id"
                    :label="detail.product.name"
                    :value="detail.id"
                  >
                  </el-option>
                </el-select>
                <el-input
                  style="margin-top:10px"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入评论"
                  autocomplete="off"
                  v-model="comment.msg"
                >
                </el-input>
              </el-form-item>
              <div style="text-align: center; margin: 10px">
                <span style="text-align: center">评分</span>
                <el-rate v-model="comment.rate"></el-rate>
              </div>
            </el-form>
            <div style="text-align: center; margin: auto">
              <el-button
                size="mini"
                @click="scope._self.$refs[`popover-${scope.$index}`].doClose();comment.detailId = ''"
              >取 消</el-button>
              <el-button
                size="mini"
                type="primary"
                @click="scope._self.$refs[`popover-${scope.$index}`].doClose();commentOrder()"
              >确 认</el-button>
            </div>
            <el-button
              style="margin-right:10px"
              size="mini"
              slot="reference"
            >评价</el-button>
          </el-popover>
          <el-popconfirm
            v-if="role == 3 && ['Shipped', 'Transit', 'Received', 'Completed'].indexOf(scope.row.status) > -1"
            @confirm="userManageOrder(scope.row)"
            :title="['Shipped', 'Transit', 'Received'].indexOf(scope.row.status) > -1 ? '是否确认收货？':'是否申请退换货？'"
          >
            <el-button
              style="margin-right:10px"
              size="mini"
              slot="reference"
            >{{['Shipped', 'Transit', 'Received'].indexOf(scope.row.status) > -1 ? '确认收货':'申请退换货'}}</el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="role == 3 && ['Ordered', 'Payed'].indexOf(scope.row.status) > -1"
            @confirm="userManageOrder(scope.row)"
            title="确定取消订单吗？"
          >
            <el-button
              size="mini"
              type="danger"
              slot="reference"
            >取消订单</el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="role == 3 && scope.row.status == 'Cancel'"
            @confirm="deleteUserOrder(scope.row)"
            title="确定删除订单吗？"
          >
            <el-button
              size="mini"
              type="danger"
              slot="reference"
            >删除订单</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="text-align: center"
      background
      :page-size="5"
      layout="prev, pager, next"
      :total="pageSize"
      @current-change="goPage"
      @prev-click="prevPage()"
      @next-click="nextPage()"
    >
    </el-pagination>
    <el-dialog
      title="订单编辑"
      width="40%"
      :visible.sync="manageDialog"
    >
      <el-form :model="manageFrom">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              v-if="role == 0"
              label="订单价格"
              label-width="30"
            >
              <el-input
                maxlength="10"
                type="text"
                show-word-limit
                v-model="manageFrom.price"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              v-else
              label="订单价格"
              label-width="30"
            >
              <el-input
                disabled
                maxlength="10"
                type="text"
                show-word-limit
                :value="manageFrom.price"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="物流公司"
              label-width="30"
            >
              <el-input
                type="text"
                v-model="manageFrom.express"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="物流单号"
              label-width="30"
            >
              <el-input
                type="text"
                v-model="manageFrom.logistics"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="支付方式"
              label-width="30"
            >
              <el-input
                :disabled="role != 0"
                maxlength="10"
                type="text"
                show-word-limit
                v-model="manageFrom.payMethod"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="manageFrom.paymentTime != null"
              label="支付时间"
              label-width="30"
            >
              <el-date-picker
                disabled
                v-model="manageFrom.paymentTime"
                type="datetime"
                placeholder="选择日期时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="订单状态"
              label-width="30"
            >
              <el-select
                v-model="manageFrom.status"
                placeholder="选择订单状态"
              >
                <el-option
                  v-for="(s,i) in orderStatus"
                  :key="i"
                  :label="s"
                  :value="i"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              prop="status"
              label="是否隐藏"
              label-width="120"
            >
              <el-switch
                v-model="manageFrom.isDelete"
                active-color="#13ce66"
                inactive-color="#ff4949"
              >
              </el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="manageDialog = false">取 消</el-button>
        <el-button
          type="primary"
          @click="manageDialog = false;manageOrder()"
        >确 定</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      orderLoading: false,
      currentPage: 0,
      pageSize: 0,
      orderStatus: {
        Ordered: '已下单',
        Cancel: '已取消',
        Payed: '已支付',
        Shipped: '已发货',
        Transit: '运输中',
        Received: '已收货',
        Completed: '已完成',
        AfterSale: '退换货中'
      },
      filterOrder: [
        { text: '已下单', value: 'Ordered' },
        { text: '已取消', value: 'Cancel' },
        { text: '已支付', value: 'Payed' },
        { text: '已发货', value: 'Shipped' },
        { text: '运输中', value: 'Transit' },
        { text: '已收货', value: 'Received' },
        { text: '已完成', value: 'Completed' },
        { text: '退换货中', value: 'AfterSale' }
      ],
      searchUser: '',
      orderArray: [],
      cellIndex: -1,
      addressDialog: false,
      editAddressDialog: false,
      manageDialog: false,
      shippeDialog: false,
      manageFrom: {
        id: '',
        price: '',
        status: [],
        express: '',
        logistics: '',
        payMethod: '',
        paymentTime: '',
        isDelete: false
      },
      expressFrom: {
        express: '',
        logistics: ''
      },
      comment: {
        detailId: '',
        user: 0,
        msg: '',
        rate: 0
      }
    }
  },
  methods: {
    searchUserOrder() {
      this.orderLoading = true
      this.$axios
        .get('/admin/order/search?username=' + this.searchUser)
        .then(res => {
          if (res.status == 200) {
            this.orderArray = res.data.data
            this.orderLoading = false
          }
        })
        .catch(() => {
          this.orderLoading = false
        })
    },
    clearSearchUserOrder() {
      this.searchUser = ''
      this.getAllOrder()
    },
    getOrderPageSize() {
      let path = "/admin"
      if (this.role == 3) {
        path = "/user"
      }
      this.$axios
        .get(path + '/order/pageSize')
        .then(res => {
          if (res.status == 200) {
            this.pageSize = res.data.data
          }
        })
        .catch(() => {})
    },
    getAllOrder() {
      this.orderLoading = true
      const path = this.role == 3 ? '/user' : '/admin'
      this.$axios
        .get(path + '/order?page=' + this.currentPage)
        .then(res => {
          if (res.status == 200) {
            this.orderArray = res.data.data
            this.orderLoading = false
          }
        })
        .catch(() => {
          this.orderLoading = false
        })
    },
    filterOrderStatus(vaule, row) {
      return row.status === vaule
    },
    shippeOrder(order) {
      const params = new URLSearchParams({
        status: 'Shipped',
        express: this.expressFrom.express,
        logistics: this.expressFrom.logistics
      })
      this.$axios
        .put('/admin/order/' + order.id, params)
        .then(res => {
          if (res.status == 200) {
            this.getAllOrder()
            this.expressFrom = {}
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    payOrder(order) {
      const params = new URLSearchParams({
        status: 'Payed',
        payMethod: '支付宝'
      })
      this.$axios
        .put('/user/order/' + order.id, params)
        .then(res => {
          if (res.status == 200) {
            this.getAllOrder()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    manageOrder() {
      const params = new URLSearchParams()
      params.append('price', this.manageFrom.price)
      params.append('status', this.manageFrom.status)
      params.append('express', this.manageFrom.express)
      params.append('logistics', this.manageFrom.logistics)
      // params.append('quantity', this.manageFrom.quantity)
      params.append('payMethod', this.manageFrom.payMethod)
      params.append('isDelete', this.manageFrom.isDelete)
      if (this.manageFrom.commentId) {
        params.append('commentId', this.manageFrom.commentId.id)
        params.append('comment', this.manageFrom.commentId.comment)
      }
      this.$axios
        .put('/admin/order/' + this.manageFrom.id, params)
        .then(res => {
          if (res.status == 200) {
            this.getAllOrder()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    userManageOrder(order) {
      let newStatus = 'Ordered'
      switch (order.status) {
        case 'Ordered':
        case 'Payed':
          newStatus = 'Cancel'
          break
        case 'Shipped':
        case 'Transit':
          newStatus = 'Completed'
          break
        case 'Completed':
          newStatus = 'AfterSale'
          break
      }
      const params = new URLSearchParams({
        status: newStatus
      })
      this.$axios
        .put('/user/order/' + order.id, params)
        .then(res => {
          if (res.status == 200) {
            this.getAllOrder()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    deleteUserOrder(order) {
      this.$axios
        .delete('/user/order/' + order.id)
        .then(res => {
          if (res.status == 200) {
            this.getAllOrder()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    filterNotComment(details) {
      return details.filter(item => item.comment == null)
    },
    commentOrder() {
      const params = new URLSearchParams({
        user: this.comment.user,
        comment: this.comment.msg,
        rate: this.comment.rate
      })
      this.$axios
        .post('/user/order/' + this.comment.detailId + '/comment', params)
        .then(res => {
          if (res.status == 200) {
            this.comment = {
              detailId: '',
              user: 0,
              msg: '',
              rate: 0
            }
            this.getAllOrder()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    loadPage() {
      if (this.searchUser.length > 0) {
        this.searchUserOrder()
      } else {
        this.getAllOrder()
      }
    },
    goPage(page) {
      this.currentPage = page - 1
      this.loadPage()
    },
    prevPage() {
      this.currentPage--
      this.loadPage()
    },
    nextPage() {
      this.currentPage++
      this.loadPage()
    }
  },
  created: function() {
    this.getAllOrder()
    this.getOrderPageSize()
  },
  computed: {
    ...mapState({
      role: state => parseInt(state.role)
    })
  }
}
</script>
<style>
  .table-search-user {
    width: 300px;
    float: right;
  }
  .table-search-user .el-input {
    width: 140px;
  }
</style>