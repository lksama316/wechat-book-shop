<template>
	<view class="tooltips">
		<view class="container">
			<view class="" @click="show = true">
				<slot ></slot>
			</view>
			<view :class="['content-wrap',direction]" v-if="show">
				<view :class="['content',direction]" :style="[customStyle]" >
					{{content}}
				</view>
			</view>
			
		</view>
		<view class="mask" @tap="show = false" v-if="show"></view>
	</view>
</template>

<script>
	export default {
		props:{
			content: {
				type: String,
				default: ''
			},
			direction: {
				type: String,
				default: 'bottom'	//	bottom bottom-right bottom-left top top-left top-right
			},
			// 用户自定义样式
			customStyle: {
				type: Object,
				default() {
					return {
						
					};
				}
			},
		},
		data() {
			return {
				show: false
			}
		},
		methods:{
		}
	}
</script>

<style lang="scss" scoped>
	.tooltips{
		overflow: visible;
		z-index: 99;
		.mask{
			position: fixed;
			top: 0;
			left: 0;
			width: 100vw;
			height: 100vh;
			opacity: 0;
			// 遮罩层用于覆盖uview中的遮罩层或弹出层
			z-index: 11000;
		}
		.container{
			position: relative;
			color: #FFFFFF;
			overflow: visible;
		}
		.content-wrap{
			position: absolute;
			z-index: 11001;
			bottom: -16rpx;
			left: 50%;
			transform: translateX(-50%);
			height: 0;
			width: 0;
			 border-width: 16rpx 8rpx 16rpx 8rpx;
			border-style: solid;
			border-color:  transparent transparent #000 transparent ;
			opacity: 0.7;
			&.top, &.top-right, &.top-left{
				bottom: 100%;
				transform: translate(-50%, 16rpx);
				border-color:   #000  transparent transparent transparent ;
			}
			
		}
		.content{
			position: absolute;
			text-align: left;
			z-index: 99;
			padding: 17rpx 27rpx;
			border-radius: 6rpx;
			line-height: 33rpx;
			width: 396rpx;
			color: #FFFFFF;
			background: #000000;
			font-size: 24rpx;
			&.bottom{
				bottom: -16rpx;
				left: 50%;
				transform: translate(-50%, 100%);
			}
			&.bottom-right{
				bottom: -16rpx;
				right: 60rpx;
				transform: translate(100%, 100%);
			}
			&.bottom-left{
				bottom: -16rpx;
				left: 60rpx;
				transform: translate(-100%, 100%);
			}
			
			&.top{
				top: -16rpx;
				left: 50%;
				transform: translate(-50%, -100%);
			}
			&.top-right{
				top: -16rpx;
				right: 60rpx;
				transform: translate(100%, -100%);
			}
			&.top-left{
				top: -16rpx;
				left: 60rpx;
				transform: translate(-100%, -100%);
			}
		}
	}
</style>
