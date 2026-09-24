package com.yjq.programmer.service;

import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-07 21:09
 */
public interface ICartService {

    // 添加购物车操作
    ResponseDTO<Boolean> addCart(CartDTO cartDTO);

    // 查询购物车数据
    ResponseDTO<List<CartDTO>> getCartList(CartDTO cartDTO);

    // 更新购物车操作
    ResponseDTO<Boolean> updateCart(CartDTO cartDTO);

    // 删除购物车操作
    ResponseDTO<Boolean> removeCart(CartDTO cartDTO);
}
