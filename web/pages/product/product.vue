<template>
	<view class="product">
		<u-toast ref="uToast"></u-toast>
		<u-search placeholder="请输入商品名称..." v-model="searchContent" :clearabled="true" @custom="searchProduct()"></u-search>
		<u-divider></u-divider>
		<view class="goods" v-for="(item, index) in productList" :key="index">
			<image :src="item.photo|filterPhoto" @click="navItemClick('/pages/product-detail/product-detail?id='+item.id)"></image>
			<view class="info">
				<view class="name" @click="navItemClick('/pages/product-detail/product-detail?id='+item.id)">
					{{item.name}}
				</view>
				<template v-if="item.isDiscount === 2">
					<view class="del" >
						原价：{{item.price}}元
					</view>
					<view class="new">
						折扣价：{{item.newPrice}}元
					</view>
				</template>
				<template v-if="item.isDiscount === 1">
					<view class="new">
						价格：{{item.price}}元
					</view>
				</template>
				<view class="sellNum">
					销量：{{item.sellNum}}
				</view>
				<image @click="addCart(item.id)" class="addToCart" src="../../static/common/cart-add.png"></image>
			</view>
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
				productList: [],
				searchContent: "",
				categoryId: "",
				cart: {
					productId: "",
					quantity: 1,
					token: ""
				}
			}
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		onLoad(option) {
			this.categoryId = option.categoryId;
			this.getAllProductList();
			uni.setNavigationBarTitle({
				title: option.text
			})
		},
		onPullDownRefresh() {
			this.getAllProductList();
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		methods: {
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async addCart(id) {
				this.cart.productId = id;
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
			async getAllProductList() {
				const res = await this.$myRuquest({
					url: '/web/product/all',
					method: 'POST',
					data: {name: this.searchContent, categoryId: this.categoryId}
				});
				if(res.data.code === 0){
					this.productList = res.data.data;
				}
			},
			searchProduct() {
				this.getAllProductList();
			}
		}

	}
</script>

<style lang="scss">
	.product {
		margin: 10rpx 0rpx;
		.goods {
			margin: 20rpx 40rpx;
			image {
				width: 200rpx;
				height: 200rpx;
				float: left;
				z-index: 11;
				position: relative;
				display: block;
				border: 2rpx #f8f8f8 solid;
			}
			.info {
				height: 200rpx;
				position: relative;
				.name {
					margin-top: 4rpx;
					margin-left: 246rpx;
				}
				.del {
					margin: 12rpx 0;
					margin-left: 246rpx;
					color: grey;
					text-decoration: line-through;
				}
				.new  {
					margin-left: 246rpx;
					margin-bottom: 12rpx;
					color: #31bd80;
				}
				.sellNum {
					margin-left: 246rpx;
					font-size: 28rpx;
					color: grey;
				}
				.addToCart{
					position: absolute;
					top: 80rpx;
					right: 20rpx;
					height: 40rpx;
					width: 40rpx;
				}
			}
		}
	}
</style>
