package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.AddressDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IAddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-10 8:43
 */
@RestController("WebAddressController")
@RequestMapping("/web/address")
public class AddressController {

    @Resource
    private IAddressService addressService;

    /**
     * 保存地址信息
     * @param addressDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveAddress(@RequestBody AddressDTO addressDTO){
        return addressService.saveAddress(addressDTO);
    }

    /**
     * 获取地址信息
     * @param addressDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<AddressDTO> getAddress(@RequestBody AddressDTO addressDTO){
        return addressService.getAddress(addressDTO);
    }
}
