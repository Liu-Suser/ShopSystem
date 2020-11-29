import axios from 'axios'
import router from '@/router'
import store from '@/store'

if (process.env.NODE_ENV == 'development') {
  axios.defaults.baseURL = 'http://localhost:8080/';
} else if (process.env.NODE_ENV == 'production') {
  axios.defaults.baseURL = 'http://localhost/api/';
}

axios.defaults.timeout = 5000
axios.defaults.retry = 2
axios.defaults.retryDelay = 100

axios.interceptors.request.use(
  config => {
    const token = store.state.token;

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response.status == 401) {
      if (router.currentRoute.path != '/' && router.currentRoute.path != '/login') {
        store.commit("removeToken")
      }
    }
    console.log(error.message)
    return Promise.reject(error)
  })

export default axios