import App from './App'
import uView from 'uview-ui'
// #ifndef VUE3
import Vue from 'vue'
import { myRequest, myFileRequest } from './util/api.js'
import './util/cache.js'
import './router/index.js'

Vue.prototype.$myRuquest = myRequest;
Vue.prototype.$myFileRuquest = myFileRequest;
Vue.config.productionTip = false;
App.mpType = 'app';
Vue.use(uView);
const app = new Vue({
    ...App
});
app.$mount();
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App);
  return {
    app
  }
}
// #endif