<template>
	<view class="product-detail">
		<u-toast ref="uToast"></u-toast>
		<view class="photo">
			<image :src="product.photo|filterPhoto" style="padding-left: 140rpx;width:500rpx;height:500rpx;"></image>
		</view>
		<view class="info">
			<view class="name">{{product.name}}</view>
			<view>
				<template v-if="product.isDiscount == 1">
					<text class="new-price">￥{{product.price}}</text>
				</template>
				<template v-if="product.isDiscount == 2">
					<text class="new-price">￥{{product.newPrice}}</text>
					<text class="old-price">￥{{product.price}}</text>
				</template>
			</view>
		</view>
		<view class="detail">
			<view class="title">商品属性信息</view>
			<u-line></u-line>
			<view>
				<u-grid :border="true" col="2">
					<u-grid-item >
						<text style="height: 80rpx;padding-left: 30rpx">上市时间：{{product.createTime}}</text>
					</u-grid-item>
					<u-grid-item>
						<text style="height: 80rpx;padding-left: 30rpx">出版社：{{product.press}}</text>
					</u-grid-item>
					<u-grid-item>
						<text style="height: 80rpx;padding-left: 30rpx">图书作者：{{product.author}}</text>
					</u-grid-item>
					<u-grid-item> 
						<text style="height: 80rpx;padding-left: 30rpx">图书字数：{{product.wordNum}}</text>
					</u-grid-item>
					<u-grid-item>
						<text style="height: 80rpx;padding-left: 30rpx">商品重量：{{product.weight}}</text>
					</u-grid-item>
					<u-grid-item>
						<text style="height: 80rpx;padding-left: 30rpx">商品库存：{{product.stock}}本</text>
					</u-grid-item>
				</u-grid>
			</view>
		</view>
		<view class="bottom">
			<u-button @click="addCart" type="primary" size="large" text="添加购物车"></u-button>
		</view>
	</view>
	
</template>

<script>
	import {BASE_URL} from "../../util/api";
	import Cache from "../../util/cache";
	import Tool from "../../util/tool";

	export default {
		data() {
			return {
				id: "",
				product: {},
				cart: {
					productId: "",
					quantity: 1,
					token: ""
				}
			}
		},
		onLoad(option) {
			this.id = option.id;
			this.cart.productId = option.id;
			this.getProductInfo();
		},
		filters: {
			filterPhoto(url) {
				if (typeof(url) == "undefined") {
					return BASE_URL +  "/photo/view?filename=common/no_image.jpg";
				}
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		methods: {
			async addCart() {
				this.cart.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
				const res = await this.$myRuquest({
					url: '/web/cart/add',
					method: 'POST',
					data: this.cart
				});
				if(res.data.code === 0){
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'success'));
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async getProductInfo() {
				const res = await this.$myRuquest({
					url: '/web/product/get',
					method: 'POST',
					data: {id: this.id}
				});
				if(res.data.code === 0){
					this.product = res.data.data;
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F1F1F1;
	}
	.photo {
		width: 750rpx;
		height: 546rpx;
		background: #FFFFFF;
	}
	.info {
		margin-top: 20rpx;
		height: 160rpx;
		background: #FFFFFF;
		.name {
			padding: 20rpx 0rpx 20rpx 30rpx;
		}
		.new-price {
			padding: 0rpx 0rpx 20rpx 20rpx;
			color: #FF0033;
			font-size: 50rpx;
			font-weight: bold;
		}
		.old-price {
			padding-left: 20rpx;
			color: grey;
			text-decoration: line-through;
		}
	}
	.detail {
		margin-top: 20rpx;
		background: #FFFFFF;
		color: #999;
		font-size: 28rpx;
		.title {
			padding: 20rpx 0rpx 20rpx 30rpx;
		}
	}
	.bottom {
		margin-top: 20rpx;
		width: 750rpx;
		background: #FFFFFF;
	}
</style>
