//只要是未登录状态，想要跳转到名单内的路径时，直接跳到登录页。
// 页面白名单，不受拦截
const whiteList = [
	'/pages/index/index',
	'/pages/login/login',
	'/pages/register/register',
	'/pages/product/product',
    '/pages/product-detail/product-detail',
];

import Tool from '../util/tool.js';
import Cache from '../util/cache.js';
 
function hasPermission (url) {
	const token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
    // 在白名单中或有登录判断条件可以直接跳转
    for(let i=0; i<whiteList.length; i++) {
        if(url.indexOf(whiteList[i]) !== -1 || !Tool.isEmpty(token)) {
            return true
        }
    }
    return false
}
 
uni.addInterceptor('navigateTo', {
    // 页面跳转前进行拦截, invoke根据返回值进行判断是否继续执行跳转
    invoke (e) {
        if(!hasPermission(e.url)){
            uni.reLaunch({
                url: '/pages/login/login'
            })
            return false
        }
        return true
    },
    success (e) {
    }
})
 
uni.addInterceptor('switchTab', {
    // tabbar页面跳转前进行拦截
    invoke (e) {
        if(!hasPermission(e.url)){
            uni.reLaunch({
                url: '/pages/login/login'
            })
            return false
        }
        return true
    },
    success (e) {
    }
})