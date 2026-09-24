<template>
	<view class="cart">
		<u-toast ref="uToast"></u-toast>
		<view class="cart-product">
			<view class="top">
				<u-checkbox-group>
					<u-checkbox @change="checkboxAll" v-if="selectAll === true" :checked=true :customStyle="{marginLeft: '20px'}" label="全选"></u-checkbox>
					<u-checkbox @change="checkboxAll" v-if="selectAll === false" :customStyle="{marginLeft: '20px'}" label="全选"></u-checkbox>
				</u-checkbox-group>
				<text class="operation" @click="changeType" v-if="type === true" >编辑</text>
				<text class="operation" @click="changeType" v-if="type === false" >完成</text>
				<u-divider :customStyle="{margin: '0rpx'}" text="分割线" :dot="true"></u-divider>
			</view>

			<view v-for="(item,index) in cartList" :key="index">
				<view class="product">
					<u-checkbox-group>
						<u-checkbox @change="checkboxOne(item.id)" v-if="checkList.find(e => e.id===item.id && e.check===false)" :customStyle="{marginLeft: '20px', marginTop: '30px'}"></u-checkbox>
						<u-checkbox @change="checkboxOne(item.id)" v-if="checkList.find(e => e.id===item.id && e.check===true)" :checked=true :customStyle="{marginLeft: '20px', marginTop: '30px'}"></u-checkbox>
					</u-checkbox-group>
					<view class="info">
						<image :src="item.productDTO.photo|filterPhoto"></image>
						<view class="name">{{item.productDTO.name}}</view>
						<view v-if="type === true">
							<template v-if="item.productDTO.isDiscount === 1">
								<text class="new">¥{{item.productDTO.price}}</text>
							</template>
							<template v-if="item.productDTO.isDiscount === 2">
								<text class="new">¥{{item.productDTO.newPrice}}</text>
								<text class="old">¥{{item.productDTO.price}}</text>
							</template>
							<text class="num">X{{item.quantity}}</text>
						</view>
						<view v-if="type === false">
							<u-button :customStyle="btnLeftStyle" type="default" @click="updateCart(item.id, item.quantity, '-')" text="-"></u-button>
							<u--text :customStyle="numStyle" :text="item.quantity"></u--text>
							<u-button :customStyle="btnRightStyle" type="default" @click="updateCart(item.id, item.quantity, '+')" text="+"></u-button>
						</view>
					</view>
					<view class="del" v-if="type === false" @click="removeCart(item.id)">
						<u-icon :customStyle="iconStyle" name="trash" size="28"></u-icon>
						<u--text type="default" :customStyle="delStyle" color="#FFFFFF" text="删除"></u--text>
					</view>
				</view>
				<u-divider :customStyle="{margin: '0px'}" text="分割线" :dot="true"></u-divider>
			</view>
			<view style="margin-top: 20rpx;">
				<u-button type="primary" @click="generateOrder" text="去结算"></u-button>
			</view>

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
				type: true,
				checkList: [],
				cartList: [],
				cartId:[],
				token: "",
				selectAll: false,
				btnLeftStyle: {
					position: 'absolute',
					top: '80rpx',
					left: '280rpx',
					border: '2rpx #d2d2d2 solid',
					borderRadius: '50%',
					width: '30rpx',
					height: '50rpx'
				},
				numStyle: {
					position: 'absolute',
					top: '84rpx',
					left: '360rpx',
					width: '50rpx'
				},
				btnRightStyle: {
					position: 'absolute',
					top: '80rpx',
					left: '400rpx',
					border: '2rpx #d2d2d2 solid',
					borderRadius: '50%',
					width: '30rpx',
					height: '50rpx'
				},
				iconStyle: {
					marginTop: '20rpx',
					marginLeft: '20rpx'
				},
				delStyle: {
					marginTop: '10rpx',
					marginLeft: '20rpx'
				}
			}
		},
		onPullDownRefresh() {
			this.getCartList();
			setTimeout(function () {
				uni.stopPullDownRefresh();  //停止下拉刷新动画
			}, 1000);
		},
		filters: {
			filterPhoto(url) {
				return BASE_URL +  "/photo/view?filename=" + url;
			}
		},
		onTabItemTap() {
		   const token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
		   if(Tool.isEmpty(token)){
			  uni.reLaunch({
				url: '/pages/login/login'
			  })
		   }
		   this.getCartList();
		},
		onLoad() {
			this.token = Cache.getCache(getApp().globalData.SESSION_KEY_LOGIN_USER);
			this.getCartList();
		},
		methods: {
			async updateCart(id, quantity, type) {
				if(type === '+') {
					quantity = quantity + 1;
				} else if(type === '-') {
					quantity = quantity - 1;
				}
				const res = await this.$myRuquest({
					url: '/web/cart/update',
					method: 'POST',
					data: {quantity:quantity, id: id, token: this.token}
				});
				if(res.data.code === 0){
					this.getCartList();
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async getCartList() {
				const res = await this.$myRuquest({
					url: '/web/cart/get',
					method: 'POST',
					data: {token: this.token}
				});
				if(res.data.code === 0){
					this.cartList = res.data.data;
					this.initCheckBox();
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async removeCart(id) {
				const res = await this.$myRuquest({
					url: '/web/cart/remove',
					method: 'POST',
					data: {id: id, token: this.token}
				});
				if(res.data.code === 0){
					this.getCartList();
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			async generateOrder() {
				this.cartId = [];
				for(let i=0; i<this.checkList.length; i++) {
					if(this.checkList[i].check) {
						this.cartId.push(this.checkList[i].id)
					}
				}
				const res = await this.$myRuquest({
					url: '/web/order/generate',
					method: 'POST',
					data: {cartList: this.cartId, token: this.token}
				});
				if(res.data.code === 0){
					this.navItemClick('/pages/order/order?id='+res.data.data);
				}else {
					this.$refs.uToast.show(Tool.messageParam(res.data.msg, 'error'));
				}
			},
			navItemClick(url){
				uni.redirectTo({
					url
				})
			},
			initCheckBox() {
				this.selectAll = false;
				this.checkList = [];
				for(let i=0; i<this.cartList.length; i++) {
					this.checkList.push({id:this.cartList[i].id, check: false});
				}
			},
			changeType(){
				this.type = !this.type;
			},
			checkboxAll(e){
				this.selectAll = e;
				if(!e){
					// 取消全选
					this.checkList = [];
					for(let i=0; i<this.cartList.length; i++) {
						this.checkList.push({id:this.cartList[i].id, check: false});
					}
				}else {
					// 选择全选
					this.checkList = [];
					for(let i=0; i<this.cartList.length; i++) {
						this.checkList.push({id:this.cartList[i].id, check: true});
					}
				}
			},
			checkboxOne(e){
				let list = [];
				for(let i=0; i<this.checkList.length; i++){
					if(this.checkList[i].id === e){
						this.checkList[i].check = !this.checkList[i].check;
						list.push(this.checkList[i]);
					}else{
						list.push(this.checkList[i]);
					}
				}
				// 判断是否全选了
				let flag = true; // 全选了
				for(let i=0; i<list.length; i++){
					if(!list[i].check){
						flag = false; //没全选
					}
				}
				this.selectAll = flag;
				this.checkList = list;
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F1F1F1;
	}
	.cart {
		.cart-product {
			height: auto;
			width: 750rpx;
			background: #FFFFFF;
		}
		.top {
			position: relative;
			padding-top: 20rpx;
			.operation {
				color: #06c1ae;
				position: absolute;
				top: 18rpx;
				right: 20rpx;
			}
		}
		.product {
			// padding: 10rpx 0rpx;
			position: relative;
			height: 156rpx;
			.info {
				image {
					width: 150rpx;
					height: 150rpx;
					position: absolute;
					top: 0rpx;
					left: 100rpx;
					border: 2rpx #f8f8f8 solid;
				}
				.name {
					position: absolute;
					top: 0rpx;
					left: 280rpx;
					color: grey;
				}
				.new {
					position: absolute;
					top: 50rpx;
					left: 280rpx;
					color: #6aab60;
				}
				.old {
					position: absolute;
					top: 100rpx;
					left: 280rpx;
					color: grey;
					text-decoration: line-through;
				}
				.num {
					position: absolute;
					top: 56rpx;
					right: 30rpx;
					color: #999999
				}
				.button {
					
				}
			}
			.del {
				width: 100rpx;
				height: 154rpx;
				background: #31bd80;
				position: absolute;
				top: 0rpx;
				right: 0rpx;
			}
		}
	}
	
</style>
