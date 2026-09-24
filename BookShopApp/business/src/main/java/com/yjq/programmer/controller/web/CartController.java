package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-07 21:08
 */
@RestController("WebCartController")
@RequestMapping("/web/cart")
public class CartController {

    @Resource
    private ICartService cartService;

    /**
     * 添加购物车操作
     * @param cartDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<Boolean> addCart(@RequestBody CartDTO cartDTO){
        return cartService.addCart(cartDTO);
    }

    /**
     * 查询购物车数据
     * @param cartDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<List<CartDTO>> getCartList(@RequestBody CartDTO cartDTO){
        return cartService.getCartList(cartDTO);
    }

    /**
     * 更新购物车操作
     * @param cartDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseDTO<Boolean> updateCart(@RequestBody CartDTO cartDTO){
        return cartService.updateCart(cartDTO);
    }

    /**
     * 删除购物车操作
     * @param cartDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeCart(@RequestBody CartDTO cartDTO){
        return cartService.removeCart(cartDTO);
    }


}
