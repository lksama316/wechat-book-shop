<template>
	<view class="member">
		<u-toast ref="uToast"></u-toast>
		<view class="info">
			<image :src="user.headPic|filterPhoto" @click="uploadPhoto"></image>

			<view class="name">{{user.username}}</view>
		</view>
		<view class="choice">
			<u-tabbar :fixed="false" @change="changeTab" :placeholder="false" :safeAreaInsetBottom="false">
				<u-tabbar-item text="我的订单" name="order" icon="order"></u-tabbar-item>
				<u-tabbar-item text="个人资料" name="account" icon="account" ></u-tabbar-item>
				<u-tabbar-item text="退出登录" name="logout" icon="info-circle"></u-tabbar-item>
			</u-tabbar>
		</view>
	</view>
</template>

<script>
	import Tool from '../../util/tool.js';
	import Cache from "../../util/cache";
	import {BASE_URL} from "../../util/api";
	export default {
		data() {
			return {
				fileList: [],
				user: {
					headPic: '',
					username: '',
					id: '',
					token: ''
				}
			}
		},
		onTabItemTap() {
		  const token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
		  if(Tool.isEmpty(token)){
			  uni.reLaunch({
				url: '/pages/login/login'
			  })
		  }
		},
		onPullDownRefresh() {
			this.getUser();
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		onLoad() {
			this.user.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
			this.getUser();
		},
		methods: {
			uploadPhoto() {
				uni.chooseImage({
					success: async (chooseImageRes) => {
						const tempFilePaths = chooseImageRes.tempFilePaths;
						if(tempFilePaths.length === 0) {
							this.$refs.uToast.show(Tool.messageParam("请至少选择一个文件！", 'error'));
							return false;
						}
						const res = await this.$myFileRuquest({
							url: '/photo/upload_photo',
							filePath: tempFilePaths[0],
							name: 'photo',
						});
						const resData = JSON.parse(res.data);
						if(resData.code === 0){
							this.user.headPic = resData.data;
							this.updateUserInfo();
						}else {
							this.$refs.uToast.show(Tool.messageParam(resData.msg, 'error'));
						}
					}
				});
			},
			async updateUserInfo() {
				const res = await this.$myRuquest({
					url: '/web/user/update',
					method: 'POST',
					data: this.user
				});
				if(res.data.code === 0){
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
					this.getUser();
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async getUser() {
				const res = await this.$myRuquest({
					url: '/web/user/get',
					method: 'POST',
					data: {token: this.user.token}
				});
				if(res.data.code === 0){
					this.user = res.data.data;
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			changeTab(e){
				switch(e){
					case 'account':
						this.navItemClick('/pages/member-info/member-info');
						break;
					case 'order':
						this.navItemClick('/pages/member-order/member-order');
						break;
					case 'logout':
						this.logout();
						break;
					default:
						break;
				}
			},
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async logout() {
				const res = await this.$myRuquest({
					url: '/web/user/logout',
					method: 'POST',
					data: {token: Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER)}
				});
				if(res.data.code === 0){
					uni.removeStorageSync(getApp().globalData.SESSION_KEY_LOGIN_USER);
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
					setTimeout(function () {
						uni.switchTab({
							url: '/pages/index/index'
						});
					}, 1000);
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F1F1F1;
	}
	.member {
		.info {
			background: #FFFFFF;
			height: 300rpx;
			width: 750rpx;
			image {
				height: 150rpx;
				width: 150rpx;
				border-radius: 50%;
				margin: 30rpx 0rpx;
				margin-left: 300rpx;
			}
			.name {
				text-align: center;
			}
		}
		.choice {
			margin-top: 20rpx;
			height: 110rpx;
			width: 750rpx;
			background: #FFFFFF;
		}
	}
</style>
