<template>
	<view class="register">
		<u-toast ref="uToast"></u-toast>
		<view class="item">
			<view class="text">用户昵称：</view>
			<u--input placeholder="请输入用户昵称" v-model="form.username" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="item">
			<view class="text">用户密码：</view>
			<u--input password placeholder="请输入用户密码" v-model="form.password" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="item">
			<view class="text">确认密码：</view>
			<u--input password placeholder="请输入确认密码" v-model="form.rePassword" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="item">
			<view class="text">用户性别：</view>
			<u-radio-group v-model="form.sex">
				<u-radio :name="1" :customStyle="radioMaleStyle"  label="男"></u-radio>
				<u-radio :name="2" :customStyle="radioFemaleStyle" label="女"></u-radio>
				<u-radio :name="3" :customStyle="radioUnKnowStyle" label="未知"></u-radio>
			</u-radio-group>
		</view>
		<view class="item">
			<view class="text">手机号码：</view>
			<u--input v-model="form.phone" placeholder="请输入手机号码" border="none" :customStyle="inputStyle"></u--input>
		</view>
		<view class="login" @click="navItemClick('/pages/login/login')">
			已有账号？快去登录！
		</view>
		<view class="button">
			<u-button type="primary" @click="registerUser" text="立即注册"></u-button>
		</view>
	</view>
</template>

<script>
	import Tool from "../../util/tool";

	export default {
		data() {
			return {
				form: {
					username: '',
					password: '',
					rePassword: '',
					sex: 3,
					phone: ''
				},
				inputStyle: {
					width: '500rpx',
					position: 'absolute',
					top: '18rpx',
					right: '20rpx'
				},
				radioMaleStyle: {
					position: 'absolute',
					top: '24rpx',
					left: '236rpx'
				},
				radioFemaleStyle: {
					position: 'absolute',
					top: '24rpx',
					left: '346rpx'
				},
				radioUnKnowStyle: {
					position: 'absolute',
					top: '24rpx',
					left: '456rpx'
				}
			}
		},
		methods: {
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async registerUser () {
				const res = await this.$myRuquest({
					url: '/web/user/register',
					method: 'POST',
					data: this.form
				});
				if(res.data.code === 0){
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
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
	.register {
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
		.login {
			color: #999;
			font-size: 24rpx;
			padding: 20rpx 0rpx;
			padding-left: 260rpx;
		}
	}
	
</style>
