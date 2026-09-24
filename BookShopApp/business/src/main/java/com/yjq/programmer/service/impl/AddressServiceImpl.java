package com.yjq.programmer.service.impl;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AddressMapper;
import com.yjq.programmer.domain.Address;
import com.yjq.programmer.domain.AddressExample;
import com.yjq.programmer.dto.AddressDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IAddressService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-10 8:43
 */
@Service
@Transactional
public class AddressServiceImpl implements IAddressService {

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private IUserService userService;

    /**
     * 保存地址信息
     * @param addressDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAddress(AddressDTO addressDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(addressDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(addressDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        Address address = CopyUtil.copy(addressDTO, Address.class);
        if(CommonUtil.isEmpty(addressDTO.getId())) {
            // 新增地址
            address.setId(UuidUtil.getShortUuid());
            address.setUserId(responseDTO.getData().getId());
            if(addressMapper.insertSelective(address) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ADDRESS_SAVE_ERROR);
            }
        } else {
            // 修改地址
            if(addressMapper.updateByPrimaryKeySelective(address) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ADDRESS_SAVE_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "地址信息保存成功！");
    }

    /**
     * 获取地址信息
     * @param addressDTO
     * @return
     */
    @Override
    public ResponseDTO<AddressDTO> getAddress(AddressDTO addressDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(addressDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        AddressExample addressExample = new AddressExample();
        addressExample.createCriteria().andUserIdEqualTo(responseDTO.getData().getId());
        Address address = new Address();
        List<Address> addressList = addressMapper.selectByExample(addressExample);
        if(addressList != null && addressList.size() == 1) {
            address = addressList.get(0);
        }
        return ResponseDTO.success(CopyUtil.copy(address, AddressDTO.class));
    }
}
