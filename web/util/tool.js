// 空校验 null或""都返回true
function isEmpty (obj) {
	if ((typeof obj == 'string')) {
		return !obj || obj.replace(/\s+/g, "") == ""
	} else {
		return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
	}
}

function messageParam(msg, type, position) {
	let data = {
		type: type,
		message: msg,
		iconUrl: 'https://cdn.uviewui.com/uview/demo/toast/'+ type +'.png',
		position: position || 'center'
	};
	return data;
}

export default {  
    isEmpty,
	messageParam
}