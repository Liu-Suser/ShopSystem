import { shallowMount } from '@vue/test-utils'
import Vue from 'vue';
import ElementUI from 'element-ui';
import LoginPage from '@/components/LoginPage.vue'

Vue.use(ElementUI)
describe('LoginPage.vue', () => {
  it('登录表单初始值为空字符串', () => {
    const wrapper = shallowMount(LoginPage, { Vue })
    expect(wrapper.vm.loginForm.username).toBe('')
    expect(wrapper.vm.loginForm.password).toBe('')
    expect(wrapper.vm.loginForm.nickname).toBe('')
    expect(wrapper.vm.loginForm.phone).toBe('')
  })
  it('页面初始为登录布局', () => {
    const wrapper = shallowMount(LoginPage)
    expect(wrapper.vm.register).toBe(false)
    expect(wrapper.vm.resetPass).toBe(false)
  })
})

