import Vue from 'vue'
import Router from 'vue-router'
import Uploader from '../components/Uploader'
import Properties from '../components/Properties'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/uploader',
      name: 'uploader',
      component: Uploader
    },
    {
      path: '/properties',
      name: 'properties',
      component: Properties,
      props: {
        properties: true
      }
    },
    { path: '*', redirect: '/uploader' }
  ]
})
