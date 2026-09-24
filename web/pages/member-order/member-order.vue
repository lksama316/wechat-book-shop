<template>
	<view class="memeber-order">
		<u-toast ref="uToast"></u-toast>
		<u-tabs @change="changeTab" :list="list1" :inactiveStyle="{color: '#BA55D3'}" itemStyle="background: #FFFFFF; height:80rpx;" :activeStyle="{color: '#ff7d00'}" lineColor="#ff7d00" lineWidth="60"></u-tabs>

		<view class="order" v-if="orderList.length > 0" v-for="(item,index) in orderList" :key="index">
			<view class="top">
				<view class="no">订单：{{item.no}}</view>
				<view class="state" v-if="item.state === 1">未支付</view>
				<view class="state" v-if="item.state === 2">已支付</view>
				<view class="state" v-if="item.state === 3">已发货</view>
				<view class="state" v-if="item.state === 4">已收货</view>
				<view class="state" v-if="item.state === 5">已取消</view>
			</view>
			<u-line></u-line>
			<view class="top">
				<view class="no">收货人姓名：{{item.receiverName || ''}}</view>
				<view class="no">收货人电话：{{item.receiverPhone || ''}}</view>
				<view class="no">收货人地址：{{item.receiverAddress || ''}}</view>
			</view>
			<u-line></u-line>
			<view>
				<view class="product" v-for="(orderItem,index2) in item.orderItemDTOList" :key="index2">
					<image :src="orderItem.productPhoto|filterPhoto"></image>
					<view class="name">{{orderItem.productName}}</view>
					<view class="price">￥{{orderItem.productPrice}}</view>
					<view class="num">X{{orderItem.quantity}}</view>
				</view>
				<u-line></u-line>
			</view>

			<view class="footer">
				<view class="total-price">
					实付：<text style="color: #BA55D3">￥{{item.totalPrice}}</text>
				</view>
			</view>
			<u-line></u-line>
			
			<view class="button" v-if="item.state === 1">
				<u-button type="error" @click="payOrder(item.id)" :customStyle="payBtnStyle" :plain="true" text="支付"></u-button>
				<u-button type="warning" :customStyle="cancelBtnStyle" @click="cancelOrder(item.id)" :plain="true" text="取消"></u-button>
			</view>
		</view>

		<view style="height: 100%" v-if="orderList.length === 0">
			<u-empty
					marginTop="150"
					textSize="20"
					iconSize="200"
					mode="order"
					icon="http://cdn.uviewui.com/uview/empty/order.png"
			>
			</u-empty>
		</view>

		
		
	</view>
</template>

<script>
	import Cache from "../../util/cache";
	import Tool from "../../util/tool";
	import {BASE_URL} from "../../util/api";

	export default {
		data() {
			return {
				 list1: [{name: '全部'}, {name: '未支付'}, {name: '已支付'}, {name: '已发货'}, {name: '已收货'}, {name: '已取消'}],
				 payBtnStyle: {
					position: 'absolute',
					width: '200rpx',
					top: '24rpx',
					right: '20rpx'
				 },
				 cancelBtnStyle: {
					 position: 'absolute',
					 width: '200rpx',
					 top: '24rpx',
					 right: '240rpx'
				 },
				 token: "",
				 state: 0,
				 orderList: []
			}
		},
		onLoad() {
			this.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
			this.getOrderList();
		},
		onPullDownRefresh() {
			this.getOrderList();
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		methods: {
			payOrder(id) {
				this.navItemClick('/pages/order/order?id='+id);
			},
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async getOrderList() {
				const res = await this.$myRuquest({
					url: '/web/order/all',
					method: 'POST',
					data: {token: this.token, state: this.state}
				});
				if(res.data.code === 0){
					this.orderList = res.data.data;
				} else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			changeTab(e) {
				this.state = e.index;
				this.getOrderList();
			},
			async cancelOrder(id) {
				const res = await this.$myRuquest({
					url: '/web/order/cancel',
					method: 'POST',
					data: {token: this.token, id: id}
				});
				if(res.data.code === 0){
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
					this.getOrderList();
				} else {
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
	.memeber-order {
		.order {
			margin: 10rpx 0rpx;
			width: 750rpx;
			height: auto;
			background: #FFFFFF;
			.top {
				padding: 20rpx;
				position: relative;
				.no {
					color: #999;
					font-size: 30rpx;
				}
				.state {
					position: absolute;
					top: 20rpx;
					font-size: 30rpx;
					right: 20rpx;
					color: #BA55D3;
				}
			}
			
			.product {
				padding: 30rpx 20rpx;
				position: relative;
				height: 130rpx;
				image {
					float: left;
					width: 160rpx;
					height: 120rpx;
				}
				.name {
					margin-top: 50rpx;
					color: #666;
					width: 80%;
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
			
			.footer {
				padding: 40rpx;
				position: relative;
				.total-price {
					float: right;
					margin-bottom: 40rpx;
					color: #999;
					font-size: 30rpx;
					font-weight: bold;
				}
			}
			
			.button {
				position: relative;
				padding: 60rpx;
			}
			
		}
	}
</style>
