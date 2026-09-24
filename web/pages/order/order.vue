<template>
	<view>
		<u-toast ref="uToast"></u-toast>
		<view class="address">
			<text class="user">收货人姓名：{{address.receiverName || ''}}</text>
			<text class="phone">收货人电话：{{address.receiverPhone || ''}}</text>
			<view class="location">收货人地址：{{address.receiverAddress || ''}}</view>
			<view class="bg" @click="changeAddress"></view>
			<view class="arrow" @click="changeAddress"> > </view>
		</view>
		<view class="order">
			<view class="title">订单信息</view>
			<view v-for="(item,index) in order.orderItemDTOList" :key="index">
				<view class="product">
					<image :src="item.productPhoto|filterPhoto"></image>
					<view class="name">{{item.productName}}</view>
					<view class="price">￥{{item.productPrice}}</view>
					<view class="num">X{{item.quantity}}</view>
				</view>
				<u-line></u-line>
			</view>
		</view>
		<view class="sum">
			<view class="price">
				<u--text type="error" :text="'合计：' + order.totalPrice + '元'"></u--text>
			</view>
		</view>
		<view style="margin-top: 30rpx;">
			<u-button :customStyle="{width: '50%', float: 'left'}" @click="toCart" type="warning" text="返回购物车"></u-button>
			<u-button :customStyle="{width: '50%'}" @click="payOrder" type="success" text="支付订单"></u-button>
		</view>

		<u-modal :show="modelShow" width="100%" :showCancelButton=true @cancel="closeModel" @confirm="confirmEdit" title="收货地址信息" >
			<view class="slot-content">
				<view style="margin-bottom: 20rpx;">
					<text style="float: left;margin-top: 10rpx;">收货人姓名：</text>
					<u--input v-model="address.receiverName" :customStyle="{width: '350rpx'}" placeholder="请输入收货人姓名" border="surround"></u--input>
				</view>
				<view style="margin-bottom: 20rpx;">
					<text style="float: left;margin-top: 10rpx;">收货人电话：</text>
					<u--input v-model="address.receiverPhone" :customStyle="{width: '350rpx'}" placeholder="请输入收货人电话" border="surround"></u--input>
				</view>
				<view style="margin-bottom: 20rpx;">
					<text style="float: left;margin-top: 10rpx;">收货人地址：</text>
					<u--input v-model="address.receiverAddress" :customStyle="{width: '350rpx'}" placeholder="请输入收货人地址" border="surround"></u--input>
				</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import Cache from "../../util/cache";
	import {BASE_URL} from "../../util/api";
	import Tool from "../../util/tool";

	export default {
		data() {
			return {
				iconStyle: {
					position: 'absolute',
					right: '50rpx',
					top: '26rpx'
				},
				order: {},
				address: {
					receiverName: "",
					receiverPhone: "",
					receiverAddress: "",
					token: "",
					id: ""
				},
				modelShow: false
			}
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		onLoad(option) {
			this.address.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
			this.order.id = option.id;
			this.getAddress();
			this.getOrder();
		},
		methods: {
			toCart(){
				uni.switchTab({
					url: '/pages/cart/cart'
				})
			},
			changeAddress() {
				this.modelShow = true;
			},
			navItemClick(url){
				uni.redirectTo({
					url
				})
			},
			async getOrder() {
				const res = await this.$myRuquest({
					url: '/web/order/get',
					method: 'POST',
					data: {id: this.order.id, token: this.address.token}
				});
				if(res.data.code === 0){
					this.order = res.data.data;
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async getAddress() {
				const res = await this.$myRuquest({
					url: '/web/address/get',
					method: 'POST',
					data: this.address
				});
				if(res.data.code === 0){
					this.address = res.data.data;
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async payOrder() {
				let _this = this;
				_this.address.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
				if(_this.address.id === null || _this.address.id === '') {
					_this.$refs.uToast.show(Tool.messageParam("收货地址不能为空", 'error'));
				}
				const res = await _this.$myRuquest({
					url: '/web/order/pay',
					method: 'POST',
					data: {addressId: _this.address.id, token: _this.address.token, id: _this.order.id}
				});
				if(res.data.code === 0){
					_this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
					setTimeout(function () {
						_this.navItemClick('/pages/member-order/member-order');
					}, 1000);
				}else {
					_this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async confirmEdit(){
				this.address.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
				const res = await this.$myRuquest({
					url: '/web/address/save',
					method: 'POST',
					data: this.address
				});
				if(res.data.code === 0){
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success', 'top'));
					this.modelShow = false;
					this.getAddress();
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error', 'top'));
				}
			},
			closeModel(){
				this.address.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
				this.modelShow = false;
				this.getAddress();
			}
		}
	}
</script>

<style lang="scss">
	.u-transition {
		background-color: rgba(0, 0, 0, 0.2) !important;
	}
	page {
		background: #F1F1F1;
	}
	.address {
		background: #333;
		height: 80rpx;
		overflow: hidden;
		padding: .30rem 3%;
		font-size: .28em;
		color: white;
		position: relative;
		.user {
			margin-right: .60rem;
		}
		.location{
			width: 80%;
		}
		.bg {
			position: absolute;
			top: 0;
			right: 0;
			height: 100%;
			line-height: 1.3rem;
			font-size: 50rpx;
			color: #f4f4f4;
			background: rgba(255,255,255,.15);
			box-shadow: -2px 0 5px grey;
			width: 3rem;
			transform: scaleX(-1);
		}
		.arrow {
			position: absolute;
			top: 12rpx;
			right: 25rpx;
			font-size: 50rpx;
			color: #f4f4f4;
		}
	}
	.order {
		color: grey;
		background: white;
		padding: 0 3%;
		margin: 20rpx 0;
		.title {
			border-bottom: 1px #d2d2d2 solid;
			padding: 20rpx 0;
			font-size: 25rpx;
		}
		.product {
			padding: 30rpx 0;
			position: relative;
			height: 130rpx;
			image {
				float: left;
				width: 160rpx;
				height: 120rpx;
			}
			.name {
				width: 80%;
				margin-top: 50rpx;
				color: #666;
				font-size: 30rpx;
			}
			.price {
				position: absolute;
				top: 64rpx;
				right: 20rpx;
				color: #666;
				font-size: 28rpx;
			}
			.num {
				position: absolute;
				top: 90rpx;
				right: 20rpx;
				color: #666;
				font-size: 26rpx;
			}
		}
	}
	.sum {
		background: white;
		width: 100%;
		height: 50rpx;
		position: relative;
		.price {
			position: absolute;
			right: 10rpx;
			top: 4rpx;
		}
	}
</style>
