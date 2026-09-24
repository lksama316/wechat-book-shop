<template>
	<view class="member-info">
		<u-toast ref="uToast"></u-toast>
		<view class="info" @click="showModel('username')">
			<text>用户昵称</text>
			<text class="setting">
				{{user.username}}
			</text>
			<u-icon :customStyle="iconStyle" color="#BA55D3" name="arrow-right"></u-icon>
		</view>
		<u-line></u-line>
		<view class="info" @click="showModel('password')">
			<text>用户密码</text>
			<text class="setting">
				******
			</text>
			<u-icon :customStyle="iconStyle" color="#BA55D3" name="arrow-right"></u-icon>
		</view>
		<u-line></u-line>
		<view class="info" @click="showModel('phone')">
			<text>手机号码</text>
			<text class="setting">
				{{user.phone}}
			</text>
			<u-icon :customStyle="iconStyle" color="#BA55D3" name="arrow-right"></u-icon>
		</view>
		<u-line></u-line>
		<view class="info" @click="showModel('sex')">
			<text>用户性别</text>
			<text class="setting">
				<template v-if="user.sex === 1">男</template>
				<template v-if="user.sex === 2">女</template>
				<template v-if="user.sex === 3">未知</template>
			</text>
			<u-icon :customStyle="iconStyle" color="#BA55D3" name="arrow-right"></u-icon>
		</view>
		
		<u-modal :show="modelShow" :showCancelButton=true @cancel="closeModel" @confirm="confirmEdit" :title="title" >
			<view class="slot-content">
				<u--input v-if="type === 'username'" v-model="user.username" :customStyle="{width: '500rpx'}" placeholder="请输入用户昵称" border="surround"></u--input>
				<u--input v-if="type === 'phone'" v-model="user.phone" :customStyle="{width: '500rpx'}" placeholder="请输入手机号码" border="surround"></u--input>
				<u--input password v-if="type === 'password'" v-model="user.password" :customStyle="{width: '500rpx'}" placeholder="请输入新密码" border="surround"></u--input>
			    <u-radio-group  v-model="user.sex" v-if="type === 'sex'">
					<u-radio :name="1" :customStyle="{padding: '0rpx 10rpx'}" label="男"></u-radio>
					<u-radio :name="2" :customStyle="{padding: '0rpx 10rpx'}" label="女"></u-radio>
					<u-radio :name="3" :customStyle="{padding: '0rpx 10rpx'}" label="未知"></u-radio>
			    </u-radio-group>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import Tool from "../../util/tool";
	import Cache from "../../util/cache";

	export default {
		data() {
			return {
				type: '',
				iconStyle: {
					position: 'absolute',
					right: '50rpx',
					top: '26rpx'
				},
				modelShow: false,
				title: '',
				user: {
					id: '',
					token: '',
					username: '',
					password: '',
					phone: '',
					sex: 3
				}
			}
		},
		onPullDownRefresh() {
			this.getUser();
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		onLoad() {
			this.user.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
			this.getUser();
		},
		methods: {
			showModel(type){
				switch(type) {
					case "username":
						this.title = "修改用户昵称";
						this.modelShow = true;
						this.type = 'username';
						break;
					case "phone":
						this.title = "修改手机号码";
						this.modelShow = true;
						this.type = 'phone';
						break;
					case "sex":
						this.title = "修改用户性别";
						this.modelShow = true;
						this.type = 'sex';
						break;
					case "password":
						this.title = "修改用户密码";
						this.modelShow = true;
						this.type = 'password';
						break;
					default:
						break;
				}
				
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
			confirmEdit(){
				this.updateUserInfo();
				this.modelShow = false;
			},
			closeModel(){
				this.modelShow = false;
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F1F1F1;
	}
	.member-info {
		.info {
			background: #FFFFFF;
			height: 60rpx;
			width: 750rpx;
			color: #BA55D3;
			padding-left: 30rpx;
			padding-top: 20rpx;
			position: relative;
			.setting {
				position: absolute;
				right: 100rpx;
				top: 20rpx;
			}
		}
	}
</style>
