import Vue from 'vue'
import App from '@/App.vue'
import axios from '@/api'
import router from '@/router'
import store from '@/store'
import ElementUI from 'element-ui';
import { format } from 'date-fns'
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);
Vue.prototype.$axios = axios
Vue.config.productionTip = false

Vue.filter('dateFormat', function (value, formatStr = 'yyyy-MM-dd HH:mm:ss') {
  return format(value, formatStr)
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
