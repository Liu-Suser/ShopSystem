import Vue from 'vue'
import Vuex from 'vuex'
import storage from '@/utils/localstorage.js'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token'),
    checkout: storage.getItem('checkout'),
    role: localStorage.getItem('role')
  },
  mutations: {
    updateToken(state, newToken) {
      localStorage.setItem('token', newToken);
      state.token = newToken;
    },
    removeToken(state) {
      localStorage.removeItem('token');
      state.token = null;
    },
    updateCheckout(state, checkout) {
      storage.setItem('checkout', checkout);
      state.checkout = checkout;
    },
    removeCheckout(state) {
      storage.removeItem('checkout');
      state.checkout = [];
    },
    updateRole(state, role) {
      localStorage.setItem('role', role);
      state.role = role;
    },
    removeRole(state) {
      localStorage.removeItem('role');
      state.role = null;
    },
  },
  actions: {
  },
  modules: {
  }
})
