<template>
	<view class="home">
		<u-toast ref="uToast"></u-toast>
		<swiper indicator-dots circular autoplay>
			<swiper-item>
				<image src="../../static/swiper/1.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image src="../../static/swiper/2.jpg"></image>
			</swiper-item>
			<swiper-item>
				<image src="../../static/swiper/3.jpg"></image>
			</swiper-item>
		</swiper>
		<!-- 导航区域 -->
		<view class="nav">
			<view class="nav_item" v-for="(item, index) in categoryList" :key="index" v-if="index < 4" @click="navItemClick('/pages/product/product?categoryId='+item.id+'&text='+item.name)">
				<view><image :src="item.photo|filterPhoto"></image></view>
				<text>{{item.name}}</text>
			</view>
		</view>
		<view class="nav">
			<view class="nav_item"  v-for="(item, index) in categoryList" :key="index" v-if="index >= 4 && index < 7" @click="navItemClick('/pages/product/product?categoryId='+item.id+'&text='+item.name)">
				<view><image :src="item.photo|filterPhoto"></image></view>
				<text>{{item.name}}</text>
			</view>
			<view class="nav_item" @click="navItemClick('/pages/product/product?categoryId=&text=全部商品')">
				<view><image src="../../static/nav/menu_bg_15.png"></image></view>
				<text>全部商品</text>
			</view>
		</view>
		<view class="tab_goods">
			<view class="tab">
			  <a @click="changeTab(1)" :class="activeClass === 1 ? 'clickStyle' : ''">热销</a>
			  <a @click="changeTab(2)" :class="activeClass === 2 ? 'clickStyle' : ''">新品</a>
			  <a @click="changeTab(3)" :class="activeClass === 3 ? 'clickStyle' : ''">打折</a>
			</view>
			<view class="goods" v-for="(item,index) in productList" :key="index">
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
	</view>
</template>

<script>
	import {BASE_URL} from "../../util/api";
	import Cache from "../../util/cache";
	import Tool from "../../util/tool";
	export default {
		data() {
			return {
				activeClass: 1,
				categoryList: [],
				productList: [],
				hotProductList: [],
				newProductList: [],
				discountProductList: [],
				cart: {
					productId: "",
					token: "",
					quantity: 1
				}
			}
		},
		onLoad() {
			this.getHotProductList();
			this.getCategoryList();
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		onPullDownRefresh() {
			this.getHotProductList();
			this.getCategoryList();
			this.activeClass = 1;
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		methods: {
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
			changeTab(e){
				this.activeClass = e;
				if(e === 1) {
					this.getHotProductList();
				} else if(e === 2) {
					this.getNewProductList();
				} else if(e === 3) {
					this.getDiscountProductList();
				}
			},
			navItemClick(url){
				uni.navigateTo({
					url
				})
			},
			async getCategoryList () {
				const res = await this.$myRuquest({
					url: '/web/category/list',
					method: 'POST',
				});
				if(res.data.code === 0){
					this.categoryList = res.data.data;
				}
			},
			async getHotProductList () {
				const res = await this.$myRuquest({
					url: '/web/product/hot',
					method: 'POST',
				});
				if(res.data.code === 0){
					this.hotProductList = res.data.data;
					this.productList = this.hotProductList;
				}
			},
			async getNewProductList () {
				const res = await this.$myRuquest({
					url: '/web/product/new',
					method: 'POST',
				});
				if(res.data.code === 0){
					this.newProductList = res.data.data;
					this.productList = this.newProductList;
				}
			},
			async getDiscountProductList () {
				const res = await this.$myRuquest({
					url: '/web/product/discount',
					method: 'POST',
				});
				if(res.data.code === 0){
					this.discountProductList = res.data.data;
					this.productList = this.discountProductList;
				}
			}
		},
		options: { styleIsolation: 'shared' }
	}
</script>

<style lang="scss">
	.home {
		swiper{
			width: 750rpx;
			height: 380rpx;
			image {
				height: 100%;
				width: 100%;
			}
		}
		.nav {
			display: flex;
			.nav_item {
				width: 25%;
				text-align: center;
				image {
					width: 100rpx;
					height: 100rpx;
					border-radius: 50%;
					margin: 20rpx 0;
				}
				text {
					font-size: 26rpx;
				}
			}
		}
		.tab_goods {
			.tab {
				display: -webkit-box;
				padding: 20rpx 0;
				border-bottom: 2rpx #f8f8f8 solid;
				a {
					display: block;
					-webkit-box-flex: 1;
					border-right: 2rpx #d2d2d2 solid;
					text-align: center;
					color: #BA55D3;
					text-decoration: none;
					outline: 0;
				}
				a:last-child {
				    border: none;
				}
				.clickStyle {
					color: #ff7d00;
				}
			}
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
		
	}
</style>
