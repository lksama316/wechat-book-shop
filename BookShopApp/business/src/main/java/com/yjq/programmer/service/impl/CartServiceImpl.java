package com.yjq.programmer.service.impl;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CartMapper;
import com.yjq.programmer.dao.ProductMapper;
import com.yjq.programmer.domain.Cart;
import com.yjq.programmer.domain.CartExample;
import com.yjq.programmer.domain.Product;
import com.yjq.programmer.dto.CartDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.ICartService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-07 21:11
 */
@Service
@Transactional
public class CartServiceImpl implements ICartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private IUserService userService;

    @Resource
    private ProductMapper productMapper;

    /**
     * 添加购物车操作
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> addCart(CartDTO cartDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(cartDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        cartDTO.setUserId(responseDTO.getData().getId());
        if(CommonUtil.isEmpty(cartDTO.getProductId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 判断商品库存是否足够
        Product product = productMapper.selectByPrimaryKey(cartDTO.getProductId());
        if(product == null) {
            return ResponseDTO.errorByMsg(CodeMsg.PRODUCT_NOT_EXIST);
        }
        if(cartDTO.getQuantity() > product.getStock()) {
            return ResponseDTO.errorByMsg(CodeMsg.PRODUCT_STOCK_EMPTY);
        }
        // 判断购物车是否已经存在
        CartExample cartExample = new CartExample();
        cartExample.createCriteria().andUserIdEqualTo(cartDTO.getUserId()).andProductIdEqualTo(cartDTO.getProductId());
        List<Cart> cartList = cartMapper.selectByExample(cartExample);
        if(cartList == null || cartList.size() == 0) {
            // 新增
            Cart cart = CopyUtil.copy(cartDTO, Cart.class);
            cart.setId(UuidUtil.getShortUuid());
            if(cartMapper.insertSelective(cart) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.CART_ADD_ERROR);
            }
        } else {
            // 修改数量
            Cart cart = cartList.get(0);
            cart.setQuantity(cart.getQuantity() + cartDTO.getQuantity());
            if(cart.getQuantity() > product.getStock()) {
                return ResponseDTO.errorByMsg(CodeMsg.PRODUCT_STOCK_EMPTY);
            }
            if(cartMapper.updateByPrimaryKeySelective(cart) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.CART_ADD_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "添加购物车成功！");
    }

    /**
     * 查询购物车数据
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<List<CartDTO>> getCartList(CartDTO cartDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(cartDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        CartExample cartExample = new CartExample();
        // select * from cart where user_id = ?
        cartExample.createCriteria().andUserIdEqualTo(responseDTO.getData().getId());
        List<Cart> cartList = cartMapper.selectByExample(cartExample);
        List<CartDTO> cartDTOList = CopyUtil.copyList(cartList, CartDTO.class);
        for(CartDTO c : cartDTOList) {
            Product product = productMapper.selectByPrimaryKey(c.getProductId());
            c.setProductDTO(CopyUtil.copy(product, ProductDTO.class));
        }
        return ResponseDTO.success(cartDTOList);
    }

    /**
     * 更新购物车操作
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateCart(CartDTO cartDTO) {
        if(CommonUtil.isEmpty(cartDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(cartDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        Cart cart = cartMapper.selectByPrimaryKey(cartDTO.getId());
        // 判断商品库存是否充足
        Product product = productMapper.selectByPrimaryKey(cart.getProductId());
        if(cartDTO.getQuantity() > product.getStock()) {
            return ResponseDTO.errorByMsg(CodeMsg.PRODUCT_STOCK_EMPTY);
        }
        // 判断购买数量是否达到下限
        if(cartDTO.getQuantity() == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CART_LEAST_ONE);
        }
        // 更新购物车信息
        cart.setQuantity(cartDTO.getQuantity());
        if(cartMapper.updateByPrimaryKeySelective(cart) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CART_QUANTITY_ERROR);
        }
        return ResponseDTO.success(true);
    }

    /**
     * 删除购物车操作
     * @param cartDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeCart(CartDTO cartDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(cartDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 删除购物车数据
        if(cartMapper.deleteByPrimaryKey(cartDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CART_DELETE_ERROR);
        }
        return ResponseDTO.success(true);
    }
}
