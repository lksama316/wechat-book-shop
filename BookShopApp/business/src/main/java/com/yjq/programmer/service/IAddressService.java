package com.yjq.programmer.service;

import com.yjq.programmer.dto.AddressDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-10 8:42
 */
public interface IAddressService {

    // 保存地址信息
    ResponseDTO<Boolean> saveAddress(AddressDTO addressDTO);

    // 获取地址信息
    ResponseDTO<AddressDTO> getAddress(AddressDTO addressDTO);
}
