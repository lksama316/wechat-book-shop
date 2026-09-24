<template>
	<view class="login">
		<u-toast ref="uToast"></u-toast>
		<view class="item">
			<view class="text">用户昵称：</view>
			<u--input placeholder="请输入用户昵称" v-model="form.username" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="item">
			<view class="text">用户密码：</view>
			<u--input password placeholder="请输入用户密码" v-model="form.password" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="register" @click="navItemClick('/pages/register/register')">
			还没有账号？点击注册！
		</view>
		<view class="button">
			<u-button type="primary" @click="loginUser" text="立即登录"></u-button>
		</view>
	</view>
</template>

<script>
	import Cache from "../../util/cache";
	import Tool from "../../util/tool";
	export default {
		data() {
			return {
				form: {
					username: '',
					password: ''
				},
				inputStyle: {
					width: '500rpx',
					position: 'absolute',
					top: '18rpx',
					right: '20rpx'
				}
			}
		},
		methods: {
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async loginUser () {
				const res = await this.$myRuquest({
					url: '/web/user/login',
					method: 'POST',
					data: this.form
				});
				if(res.data.code === 0){
					// 同步存储数据
					Cache.setCache(getApp().globalData.SESSION_KEY_LOGIN_USER, res.data.data.token, 3600);
					setTimeout(function () {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 1000);
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F1F1F1;
	}
	.login {
		.item {
			margin: 10rpx 0rpx;
			height: 80rpx;
			background: #FFFFFF;
			position: relative;
			.text {
				padding-top: 20rpx;
				padding-left: 30rpx;
			}
		}
		.register {
			color: #999;
			font-size: 24rpx;
			padding: 20rpx 0rpx;
			padding-left: 260rpx;
		}
	}
</style>
