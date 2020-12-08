<template>
  <el-main>
    <el-form
      :model="loginForm"
      status-icon
      :rules="rules"
      label-width="100px"
      class="login-form"
    >
      <el-form-item
        label="用户名"
        prop="username"
      >
        <el-input
          type="text"
          v-model="loginForm.username"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-show="register"
        label="昵称"
        prop="nickname"
      >
        <el-input
          type="text"
          v-model="loginForm.nickname"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-show="resetPass == false"
        label="密码"
        prop="password"
      >
        <el-input
          type="password"
          v-model="loginForm.password"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-show="register"
        label="手机号"
        prop="phone"
      >
        <el-input
          v-model.number="loginForm.phone"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-popover
          v-model="resetPop"
          placement="bottom"
          width="200"
        >
          <el-form :model="resetForm">
            <el-form-item
              label="密保问题"
              label-width="120"
            >
              <el-input
                disabled
                autocomplete="off"
                v-model="resetQuention"
              >
              </el-input>
            </el-form-item>
            <el-form-item
              label="密保答案"
              label-width="120"
            >
              <el-input
                autocomplete="off"
                v-model="resetForm.answer"
              >
              </el-input>
            </el-form-item>
            <el-form-item
              label="新密码"
              label-width="120"
            >
              <el-input
                type="password"
                autocomplete="off"
                v-model="resetForm.password"
              >
              </el-input>
            </el-form-item>
          </el-form>
          <div style="text-align: center; margin: auto">
            <el-button
              size="mini"
              type="primary"
              @click="resetPassword()"
            >提 交</el-button>
          </div>
          <el-button
            v-show="resetPass == true && register == false"
            class="button"
            type="primary"
            slot="reference"
            @click="getResetQuention()"
          >重置密码</el-button>
        </el-popover>
      </el-form-item>
      <el-form-item>
        <el-button
          v-show="register == false && resetPass == false"
          class="button"
          type="primary"
          @click="submitForm()"
        >登录</el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          v-show="register == true && resetPass == false"
          class="button"
          type="primary"
          @click="goRegister()"
        >注册</el-button>
      </el-form-item>
      <el-form-item>
        <span style="float:right">
          <el-button
            v-show="resetPass == false && register == false"
            type="text"
            @click="register = true;resetPass = false"
          >未注册？</el-button>
          <el-button
            v-show="register == false && resetPass == false"
            @click="resetPass = true;register = false"
            type="text"
          >找回密码</el-button>
          <el-button
            v-show="register == true || resetPass == true"
            @click="register = false;resetPass = false"
            type="text"
          >返回</el-button>
        </span>
      </el-form-item>
    </el-form>
  </el-main>
</template>

<script>
export default {
  data() {
    return {
      register: false,
      resetPass: false,
      resetPop: false,
      loginForm: {
        username: '',
        password: '',
        nickname: '',
        phone: ''
      },
      resetQuention: '',
      resetForm: {
        username: '',
        answer: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
      }
    }
  },
  methods: {
    submitForm() {
      if (!this.loginForm.username) {
        this.$message.error('请输入用户名！')
        return
      } else if (!this.loginForm.password) {
        this.$message.error('请输入密码！')
        return
      } else {
        const params = new URLSearchParams({
          username: this.loginForm.username,
          password: this.loginForm.password
        })
        this.$axios
          .post('/auth/login', params)
          .then(res => {
            if (res.status == 200) {
              this.$message.success('登录成功！')
              this.$store.commit('updateRole', res.data.role)
              this.$store.commit('updateToken', res.data.token)
              if ([0, 1, 2].indexOf(res.data.role) > -1) {
                this.$router.push({ path: '/dashboard' })
              } else {
                this.$router.push({ path: '/user' })
              }
            }
          })
          .catch(err => {
            if (err.response.status == 401) {
              this.$message.error('输入的用户名或密码错误！')
            }
          })
      }
    },
    goRegister() {
      const params = new URLSearchParams({
        username: this.loginForm.username,
        password: this.loginForm.password,
        nickname: this.loginForm.nickname,
        phone: this.loginForm.phone
      })
      this.$axios
        .post('/auth/signup', params)
        .then(res => {
          if (res.status == 200) {
            this.$message.success('注册成功！')
            this.register = false
          }
        })
        .catch(() => {
          this.$message.error('注册失败，请稍后重试！')
        })
    },
    getResetQuention() {
      this.$axios
        .get('/auth/question?username=' + this.loginForm.username)
        .then(res => {
          if (res.status == 200) {
            this.$message.success('获取密保问题成功！')
            this.resetQuention = res.data.msg
          }
        })
        .catch(() => {
          this.resetQuention = ''
          this.resetPop = false
          this.$message.error('获取密保问题失败！')
        })
    },
    resetPassword() {
      const params = new URLSearchParams({
        username: this.loginForm.username,
        answer: this.resetForm.answer,
        newPassword: this.resetForm.password
      })
      this.$axios
        .put('/auth/forget', params)
        .then(res => {
          if (res.status == 200) {
            this.$message.success('重置密码成功！')
            this.resetPass = false
            this.resetPop = false
          }
        })
        .catch(() => {
          this.resetPop = false
          this.$message.error('重置密码失败，请稍后重试！')
        })
    }
  },
  created: function() {}
}
</script>

<style>
  .login-form {
    width: 400px;
    margin: auto;
  }
  .button {
    width: 100%;
    display: block;
  }
</style>
