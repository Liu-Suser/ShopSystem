<template>
  <el-container>
    <el-aside
      v-loading="userInfoLoading"
      width="220px"
      class="user-info"
    >
      <el-card class="user-card">
        <div class="user-icon">
          <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
          <h4>{{userInfo.username}}</h4>
        </div>
        <div class="user-detail">
          <h4 v-show="role == 3">积分<i class="el-icon-coin"></i> {{userInfo.userPoint}}</h4>
          <h4>手机<i class="el-icon-phone-outline"></i> {{userInfo.phone}}</h4>
          <h4>权限<i class="el-icon-user"></i> {{userRole[role]}}</h4>
        </div>
        <div class="user-address">
          <div v-show="role == 3">
            <h4>默认地址<i class="el-icon-map-location"></i></h4>
            <h5>{{address.name + ' ' + address.phone}}</h5>
            <h5>{{address.address}}</h5>
          </div>
        </div>
        <div class="user-action">
          <el-link
            v-if="role == 0"
            :underline="false"
            @click="userManageDialog = !userManageDialog"
          >用户管理</el-link>
          <el-link
            v-show="role == 3"
            :underline="false"
            @click="addressDialog = !addressDialog"
          >地址管理</el-link>
          <el-link
            v-show="role == 3"
            :underline="false"
            @click="editUserInfoDialog = !editUserInfoDialog"
          >信息修改</el-link>
          <el-link
            v-show="[0,1].indexOf(role) > -1"
            :underline="false"
            @click="productDialog = !productDialog"
          >商品管理</el-link>
          <el-link
            v-show="[0,1].indexOf(role) > -1"
            :underline="false"
            @click="categoryDialog = !categoryDialog"
          >分类管理</el-link>
          <el-link
            :underline="false"
            @click="resetDialog = !resetDialog"
          >修改密码</el-link>
          <el-link
            v-show="role == 3"
            :underline="false"
            @click="logout()"
          >退出登录</el-link>
        </div>
      </el-card>
      <el-dialog
        v-if="role == 3"
        width="50%"
        title="地址管理"
        :visible.sync="addressDialog"
      >
        <el-table
          height="250"
          :data="addressArray"
          style="width: 100%"
        >
          <el-table-column
            label="收货人姓名"
            width="90"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="收货人手机"
            width="120"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.phone }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="收货人地址"
            width="220"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.address }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="是否默认"
            width="80"
          >
            <template slot-scope="scope">
              <el-tag
                v-if="scope.row.default"
                size="medium"
              >是</el-tag>
              <el-tag
                v-else
                type="info"
                size="medium"
              >否</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editAddressDialog = !editAddressDialog; editAddress = scope.row"
              >编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          width="40%"
          title="编辑地址"
          :visible.sync="editAddressDialog"
          append-to-body
        >
          <el-form :model="editAddress">
            <el-form-item
              label="收货人姓名"
              label-width="120"
            >
              <el-input
                v-model="editAddress.name"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="收货人手机"
              label-width="120"
            >
              <el-input
                v-model="editAddress.phone"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="收件人地址"
              label-width="120"
            >
              <el-input
                v-model="editAddress.address"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="默认地址"
              label-width="120"
            >
              <el-switch
                v-model="editAddress.default"
                active-color="#13ce66"
                inactive-color="#ff4949"
              >
              </el-switch>
            </el-form-item>

          </el-form>
          <div
            slot="footer"
            class="dialog-footer"
          >
            <el-button
              v-if="editAddress.id"
              style="float:left"
              type="danger"
              @click="editAddressDialog = false;deleteUserAddress()"
            >删 除</el-button>
            <el-button @click="editAddressDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="editAddressDialog = false;saveUserAddress()"
            >保 存</el-button>
          </div>
        </el-dialog>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button
            type="primary"
            @click="editAddressDialog = true"
          >新建地址</el-button>
          <el-button @click="addressDialog = false">关 闭</el-button>
        </div>
      </el-dialog>
      <el-dialog
        v-if="role == 3"
        width="40%"
        title="编辑信息"
        :visible.sync="editUserInfoDialog"
        append-to-body
      >
        <el-form :model="editUserInfo">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item
                label="昵称"
                label-width="120"
              >
                <el-input
                  v-model="editUserInfo.nickname"
                  autocomplete="off"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="手机号"
                label-width="120"
              >
                <el-input
                  v-model="editUserInfo.phone"
                  autocomplete="off"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="安全问题"
                label-width="120"
              >
                <el-input
                  v-model="editUserInfo.question"
                  autocomplete="off"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="问题答案"
                label-width="120"
              >
                <el-input
                  v-model="editUserInfo.answer"
                  autocomplete="off"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button @click="editUserInfoDialog = false">取 消</el-button>
          <el-button
            type="primary"
            @click="editUserInfoDialog = false;updateUserInfo()"
          >保 存</el-button>
        </div>
      </el-dialog>
      <el-dialog
        v-if="[0,1].indexOf(role) > -1"
        height="250"
        width="60%"
        title="商品管理"
        :visible.sync="productDialog"
      >
        <el-table
          :data="productArray"
          style="width: 100%"
        >
          <el-table-column
            label="图片"
            width="80"
          >
            <template slot-scope="scope">
              <el-avatar
                shape="square"
                :size="50"
                fit="fill"
                :src="scope.row.image.length == 0 ? null : scope.row.image[0].url"
              ></el-avatar>
            </template>
          </el-table-column>
          <el-table-column
            label="名称"
            width="120"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="二级标题"
            width="120"
          >
            <template slot-scope="scope">
              <h6>{{ scope.row.subtitle }}</h6>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="详情信息"
            width="150"
          >
            <template slot-scope="scope">
              <h6>{{scope.row.detail}}</h6>
            </template>
          </el-table-column>
          <el-table-column
            label="价格"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag size="small">{{scope.row.price.toFixed(2)}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            label="库存"
            width="80"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.stock }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editProductDialog = !editProductDialog; editProduct = scope.row"
              >编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          width="40%"
          title="编辑商品"
          :visible.sync="editProductDialog"
          append-to-body
        >
          <el-form
            :ref="editProduct"
            :model="editProduct"
          >
            <el-form-item
              prop="image"
              label="图片"
              label-width="120"
            >
              <el-upload
                class="upload-demo"
                action=""
                :on-remove="removeImage"
                :http-request="uploadImage"
                :file-list="editProduct.image"
                list-type="picture"
                :limit="5"
              >
                <el-button
                  size="small"
                  type="primary"
                >点击上传</el-button>
                <div
                  slot="tip"
                  class="el-upload__tip"
                >只能上传jpg/jpeg/png文件，且不超过5MB</div>
              </el-upload>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  prop="name"
                  label="名称"
                  label-width="120"
                >
                  <el-input
                    v-model="editProduct.name"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  prop="subtitle"
                  label="二级标题"
                  label-width="120"
                >
                  <el-input
                    v-model="editProduct.subtitle"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  prop="price"
                  label="价格"
                  label-width="120"
                >
                  <el-input
                    v-model="editProduct.price"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  prop="stock"
                  label="库存"
                  label-width="120"
                >
                  <el-input
                    v-model="editProduct.stock"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item
              prop="detail"
              label="详情信息"
              label-width="120"
            >
              <el-input
                type="textarea"
                :rows="3"
                placeholder="请输入详情信息"
                autocomplete="off"
                v-model="editProduct.detail"
              >
              </el-input>
            </el-form-item>
            <el-form-item
              prop="categoryId.id"
              label="商品分类"
            >
              <el-select
                v-model="editProduct.categoryId.id"
                placeholder="请选择商品分类"
              >
                <el-option-group
                  v-for="a in categoryArray"
                  :key="a.id"
                  :label="a.name"
                >
                  <el-option
                    v-for="b in a.children"
                    :key="b.id"
                    :label="b.name"
                    :value="b.id"
                  >
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item
              prop="status"
              label="是否上架"
              label-width="120"
            >
              <el-switch
                v-model="editProduct.status"
                active-color="#13ce66"
                inactive-color="#ff4949"
              >
              </el-switch>
            </el-form-item>
          </el-form>
          <div
            slot="footer"
            class="dialog-footer"
          >
            <!-- <el-button
              style="float:left"
              type="danger"
              @click="editProductDialog = false"
            >删 除</el-button> -->
            <el-button @click="editProductDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="editProductDialog = false;saveProduct()"
            >保 存</el-button>
          </div>
        </el-dialog>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <div style="float:left">
            <el-input
              v-model="search"
              placeholder="请输入商品名称"
              clearable
            >
              <el-button
                slot="append"
                size="small"
                icon="el-icon-search"
                @click="searchProduct()"
              ></el-button>
            </el-input>
          </div>

          <el-button
            type="primary"
            @click="editProductDialog = true;resetFormText(editProduct)"
          >新增商品</el-button>
          <el-button @click="productDialog = false">关 闭</el-button>
        </div>
      </el-dialog>
      <el-dialog
        v-if="[0,1].indexOf(role) > -1"
        width="50%"
        title="分类管理"
        :visible.sync="categoryDialog"
      >
        <el-table
          :data="categoryArray"
          style="width: 100%"
          row-key="id"
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          >
          <el-table-column
            prop="name"
            label="分类名称"
            width="200"
          >
            <template slot-scope="scope">
              {{scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column
            prop="parentId"
            label="父分类"
            width="220"
          >
            <template slot-scope="scope">
              {{scope.row.parent}}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editCategoryDialog = true;editCategory = scope.row"
              >编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          width="50%"
          title="编辑分类"
          :visible.sync="editCategoryDialog"
          append-to-body
        >
          <el-form :model="editCategory">
            <el-row :gutter="20">
              <el-col :span="10">
                <el-form-item
                  label="名称"
                  label-width="120"
                >
                  <el-input
                    placeholder="请输入名称"
                    autocomplete="off"
                    v-model="editCategory.name"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item
                  label="父分类"
                  label-width="120"
                >
                  <el-select
                    :disabled="editCategory.parentId == 0"
                    v-model="editCategory.parentId"
                    placeholder="请选择父分类"
                  >
                    <el-option
                      v-for="a in categoryArray"
                      :key="a.id"
                      :label="a.name"
                      :value="a.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item
                  label="是否根分类"
                  label-width="80"
                >
                  <el-switch
                    :disabled="editCategory.children != null"
                    v-model="editCategory.parentId"
                    :active-value="0"
                    :inactive-value="1"
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
            <el-button @click="editCategoryDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="editCategoryDialog = false;saveCategory()"
            >保 存</el-button>
          </div>
        </el-dialog>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <el-button
            type="primary"
            @click="editCategoryDialog = true;editCategory = {}"
          >新建分类</el-button>
          <el-button @click="categoryDialog = false">关 闭</el-button>
        </div>
      </el-dialog>
      <el-dialog
        width="40%"
        title="修改密码"
        :visible.sync="resetDialog"
      >
        <el-form
          :model="resetForm"
          status-icon
          :rules="rules"
          ref="resetForm"
          label-width="100px"
        >
          <el-form-item
            label="密码"
            prop="pass"
          >
            <el-input
              type="password"
              v-model="resetForm.pass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="确认密码"
            prop="checkPass"
          >
            <el-input
              type="password"
              v-model="resetForm.checkPass"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetFormText('resetForm')">重置</el-button>
            <el-button
              type="primary"
              @click="resetPassword('resetForm')"
            >提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-dialog
        v-if="[0,1].indexOf(role) > -1"
        width="50%"
        title="用户管理"
        :visible.sync="userManageDialog"
      >
        <el-table
          height="300"
          :data="userArray"
          style="width: 100%"
        >
          <el-table-column
            label="用户名"
            width="80"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.username }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="昵称"
            width="80"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.nickname }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="积分"
            width="80"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.userpoint }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="手机号"
            width="80"
          >
            <template slot-scope="scope">
              <el-tag size="medium">{{scope.row.phone}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            label="安全问题"
            width="120"
          >
            <template slot-scope="scope">
              <h5>{{ scope.row.question }}</h5>
            </template>
          </el-table-column>
          <el-table-column
            label="权限"
            width="80"
          >
            <template slot-scope="scope">
              <el-tag size="mini">{{userRole[scope.row.role]}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="editUserDialog = !editUserDialog; editUser = scope.row"
              >编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          width="50%"
          title="编辑用户"
          :visible.sync="editUserDialog"
          append-to-body
        >
          <el-form :model="editUser">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item
                  label="用户名"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.username"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="昵称"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.nickname"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="密码"
                  label-width="120"
                >
                  <el-input
                    type="password"
                    v-model="editUser.pass"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item
                  label="手机号"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.phone"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="安全问题"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.question"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="安全问题答案"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.answer"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="积分"
                  label-width="120"
                >
                  <el-input
                    v-model="editUser.userpoint"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item
                  label="权限"
                  label-width="120"
                >
                  <el-select
                    v-model="editUser.role"
                    placeholder="请选择用户权限"
                  >
                    <el-option
                      v-for="(r,i) in userRole"
                      :key="i"
                      :label="r"
                      :value="i"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div
            slot="footer"
            class="dialog-footer"
          >
            <el-button
              v-if="editUser.id != null"
              style="float:left"
              type="danger"
              @click="editUserDialog = false;deleteUser()"
            >删 除</el-button>
            <el-button @click="editUserDialog = false">取 消</el-button>
            <el-button
              type="primary"
              @click="editUserDialog = false;saveUser()"
            >保 存</el-button>
          </div>
        </el-dialog>
        <div
          slot="footer"
          class="dialog-footer"
        >
          <div style="float:left">
            <el-input
              v-model="searchUser"
              placeholder="请输入用户名"
              clearable
            >
              <el-button
                slot="append"
                size="small"
                icon="el-icon-search"
                @click="searchUsername"
              ></el-button>
            </el-input>
          </div>
          <el-button
            type="primary"
            @click="editUserDialog = true;editUser = {}"
          >新建用户</el-button>
          <el-button @click="userManageDialog = false">关 闭</el-button>
        </div>
      </el-dialog>
    </el-aside>
    <el-main v-if="[0,1].indexOf(role) > -1">
      <el-tabs
        value="chart"
        stretch
      >
        <el-tab-pane
          label="统计图表"
          name="chart"
        >
          <StatisticChart />
        </el-tab-pane>
        <el-tab-pane
          label="订单列表"
          name="order"
        >
          <OrderTable />
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <OrderTable v-else />
  </el-container>
</template>

<script>
import { mapState } from 'vuex'
import StatisticChart from '@/components/StatisticChart.vue'
import OrderTable from '@/components/OrderTable.vue'
export default {
  components: {
    StatisticChart,
    OrderTable
  },
  data() {
    return {
      userInfoLoading: false,
      userInfo: {},
      address: {
        id: 0,
        name: '',
        phone: '',
        address: '无'
      },
      search: '',
      searchUser: '',
      categoryArray: [],
      editCategory: {},
      addressArray: [],
      editAddress: {
        default: false
      },
      editUserInfo: {},
      productArray: [],
      editProduct: {
        image: [],
        name: '',
        subtitle: '',
        price: 0,
        stock: 0,
        detail: '',
        status: true,
        categoryId: {
          id: ''
        }
      },
      userArray: [],
      editUser: {},
      userRole: ['管理员', '客服', '仓库管理员', '普通会员'],
      addressDialog: false,
      editAddressDialog: false,
      editUserInfoDialog: false,
      categoryDialog: false,
      editCategoryDialog: false,
      productDialog: false,
      editProductDialog: false,
      resetDialog: false,
      userManageDialog: false,
      editUserDialog: false,
      resetForm: {
        pass: '',
        checkPass: ''
      },
      rules: {
        pass: [
          {
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('请输入密码'))
              } else {
                if (this.resetForm.checkPass !== '') {
                  this.$refs.resetForm.validateField('checkPass')
                }
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        checkPass: [
          {
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('请再次输入密码'))
              } else if (value !== this.resetForm.pass) {
                callback(new Error('两次输入密码不一致!'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    getUserInfo() {
      this.userInfoLoading = true
      this.$axios
        .get('/user/info')
        .then(res => {
          if (res.status == 200) {
            this.userInfo = res.data.data
            if (this.userInfo.role == 3) {
              this.getUserAddres()
              this.editUserInfo = this.userInfo
            }
            if ([0, 1].indexOf(this.userInfo.role) > -1) {
              this.getAllCategory()
            }
            this.userInfoLoading = false
          }
        })
        .catch(() => {
          this.userInfoLoading = false
          this.$message.error('获取信息失败！跳转主页中...')
          setTimeout(() => {
            this.$router.push({ path: '/' })
          }, 1000)
        })
    },
    getAllUser() {
      this.$axios
        .get('/admin/user')
        .then(res => {
          if (res.status == 200) {
            this.userArray = res.data.data
          }
        })
        .catch(() => {})
    },
    searchUsername() {
      this.$axios
        .get('/admin/user/search?username=' + this.searchUser)
        .then(res => {
          if (res.status == 200) {
            this.userArray = res.data.data
          }
        })
        .catch(() => {})
    },
    getPostUserParams() {
      return new URLSearchParams({
        username: this.editUser.username,
        nickname: this.editUser.nickname,
        password: this.editUser.pass,
        phone: this.editUser.phone,
        question: this.editUser.question,
        answer: this.editUser.answer,
        role: this.editUser.role
      })
    },
    saveUser() {
      const params = this.getPostUserParams()
      let method = 'post'
      let url = '/admin/user/'
      if (this.editUser.id) {
        method = 'put'
        url += this.editUser.id
      }
      this.$axios({
        method: method,
        url: url,
        data: params
      })
        .then(res => {
          if (res.status == 200) {
            this.$message.success(res.data.msg)
            this.searchUsername()
          }
        })
        .catch(() => {
          this.$message.error('保存失败！')
        })
    },
    updateUserInfo() {
      const params = new URLSearchParams({
        nickname: this.editUserInfo.nickname,
        phone: this.editUserInfo.phone,
        question: this.editUserInfo.question,
        answer: this.editUserInfo.answer
      })
      this.$axios
        .put('/user/info/', params)
        .then(res => {
          if (res.status == 200) {
            this.$message.success(res.data.msg)
            this.getUserInfo()
          }
        })
        .catch(() => {
          this.$message.error('修改失败！')
        })
    },
    deleteUser() {
      this.$axios
        .delete('/admin/user/' + this.editUser.id)
        .then(res => {
          if (res.status == 200) {
            this.searchUsername()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('删除失败！')
        })
    },
    getAllCategory() {
      this.$axios
        .get('/category')
        .then(res => {
          if (res.status == 200) {
            this.categoryArray = []
            this.filterCategory(res.data.data)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    filterCategory(res) {
      let childArray = []
      while (res.length > 0) {
        let r = res.pop()
        if (r.parentId == 0) {
          this.categoryArray.push({
            id: r.id,
            name: r.name,
            status: r.status,
            parent: '根分类',
            parentId: 0,
            children: []
          })
        } else {
          childArray.push(r)
        }
      }
      this.categoryArray.map(item => {
        item.children = childArray.filter(function(child) {
          return child.parentId == item.id
        })
      })
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
          }
        })
        .catch(() => {})
    },
    getAllProduct(n) {
      this.$axios
        .get('/product?page=' + n)
        .then(res => {
          if (res.status == 200) {
            this.productArray = res.data.data
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    searchProduct() {
      const params = new URLSearchParams({
        product: this.search
      }).toString()
      this.$axios
        .get('/product/search?' + params)
        .then(res => {
          if (res.status == 200) {
            this.productArray = res.data.data
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    saveCategory() {
      let method = 'post'
      let url = '/admin/category/'
      if (this.editCategory.id) {
        method = 'put'
        url += this.editCategory.id
      }
      const params = new URLSearchParams({
        name: this.editCategory.name,
        status: true,
        parentId: this.editCategory.parentId
      })
      this.$axios({
        method: method,
        url: url,
        data: params
      })
        .then(res => {
          if (res.status == 200) {
            this.$message.success(res.data.msg)
            this.getAllCategory()
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    deleteUserAddress() {
      this.$axios
        .delete('/user/address/' + this.editAddress.id)
        .then(res => {
          if (res.status == 200) {
            this.getUserAddres()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('删除地址失败！')
        })
    },
    saveUserAddress() {
      let method = 'post'
      let url = '/user/address/'
      if (this.editAddress.id) {
        method = 'put'
        url += this.editAddress.id
      }
      const params = new URLSearchParams({
        name: this.editAddress.name,
        phone: this.editAddress.phone,
        address: this.editAddress.address,
        isDefault: this.editAddress.default
      })
      this.$axios({
        method: method,
        url: url,
        data: params
      })
        .then(res => {
          if (res.status == 200) {
            this.getUserInfo()
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('修改失败！')
        })
    },
    imageToStr(images) {
      if (images && images.length > 0) {
        let str = '['
        for (let index = 0; index < images.length; index++) {
          str += '{"url": "' + images[index].url + '"}'
          if (index != images.length - 1) {
            str += ','
          }
        }
        return (str += ']')
      }
      return '[]'
    },
    saveProduct() {
      let method = 'post'
      let url = '/admin/product/'
      if (this.editProduct.id) {
        method = 'put'
        url += this.editProduct.id
      }
      const params = new URLSearchParams({
        categoryId: this.editProduct.categoryId.id,
        name: this.editProduct.name,
        subtitle: this.editProduct.subtitle,
        image: this.imageToStr(this.editProduct.image),
        detail: this.editProduct.detail,
        price: this.editProduct.price,
        stock: this.editProduct.stock,
        status: this.editProduct.status
      })
      this.$axios({
        method: method,
        url: url,
        data: params
      })
        .then(res => {
          if (res.status == 200) {
            this.$message.success(res.data.msg)
          }
        })
        .catch(() => {
          this.$message.error('操作失败，请稍后重试！')
        })
    },
    removeImage(file, fileList) {
      this.editProduct.image = fileList.filter(function(image) {
        return image !== file
      })
    },
    uploadImage(param) {
      const fromData = new FormData()
      fromData.append('file', param.file)
      this.$axios
        .post('/admin/product/uploadImage', fromData)
        .then(res => {
          if (res.status == 200) {
            this.editProduct.image.push({
              url: this.$axios.defaults.baseURL + res.data.msg
            })
            this.$message.success('上传成功！')
          }
        })
        .catch(() => {
          this.$message.error('上传失败，请稍后重试！')
        })
    },
    resetPassword(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          const params = new URLSearchParams()
          params.append('newPassword', this.resetForm.pass)
          this.$axios
            .put('/user/password', params)
            .then(res => {
              if (res.status == 200) {
                this.$message.success('修改密码成功！')
              }
            })
            .catch(() => {
              this.$message.error('操作失败，请稍后重试！')
            })
        } else {
          return false
        }
      })
    },
    resetFormText(formName) {
      this.$nextTick(() => {
        this.$refs[formName].resetFields()
      })
    },
    logout() {
      this.$message.success('退出登录成功！跳转主页中...')
      this.$store.commit('removeToken')
      setTimeout(() => {
        this.$store.commit('removeRole')
        this.$store.commit('removeCheckout')
        this.$router.push({ path: '/' })
      }, 1000)
    }
  },
  created: function() {
    this.getUserInfo()
  },
  computed: {
    ...mapState({
      role: state => parseInt(state.role)
    })
  }
}
</script>

<style>
  .user-info {
    margin: 20px 0px 0px 50px;
  }
  .user-card {
    width: 200px;
  }
  .user-icon {
    width: 100px;
    text-align: center;
    margin: auto;
  }
  .user-detail {
    height: 90px;
  }
  .user-detail h4 {
    margin: 10px;
  }
  .user-address {
    height: 100px;
    margin: 10px;
  }
  .user-address h6 {
    margin: 0;
  }
  .user-action a {
    margin: 10px;
  }
</style>
