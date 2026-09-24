export const BASE_URL = 'http://localhost:8081';
export const myRequest = (options)=>{
	return new Promise((resolve,reject)=>{
		uni.request({
			url:BASE_URL + options.url,
			method: options.method || 'GET',
			data: options.data || {},
			success: (res)=>{
				if(res.statusCode !== 200) {
					return uni.showToast({
						icon: 'error',
						title: '获取数据失败'
					})
				}
				resolve(res)
			},
			fail: (err)=>{
				uni.showToast({
					icon: 'error',
					title: '请求接口失败'
				});
				reject(err)
			}
		})
	})
};

export const myFileRequest = (options)=>{
	return new Promise((resolve,reject)=>{
		uni.uploadFile({
			url: BASE_URL + options.url,
			filePath: options.filePath,
			name: options.name,
			success: (res)=>{
				if(res.statusCode !== 200) {
					return uni.showToast({
						icon: 'error',
						title: '获取数据失败'
					})
				}
				resolve(res)
			},
			fail: (err)=>{
				uni.showToast({
					icon: 'error',
					title: '请求接口失败'
				});
				reject(err)
			}
		})
	})
};

