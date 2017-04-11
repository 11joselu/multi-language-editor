// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueResource from 'vue-resource'
import 'normalize.css/normalize.css'
import 'foundation-sites/dist/css/foundation.min.css'


Vue.use(VueResource)

Vue.config.productionTip = false

Vue.filter('searchFilter', function (value, search) {
  return value;
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
