package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.dao.my.MyOrderMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.DiscountEnum;
import com.yjq.programmer.enums.OrderStateEnum;
import com.yjq.programmer.service.IOrderService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.SnowFlake;
import com.yjq.programmer.util.UuidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-09 17:06
 */
@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

    @Resource
    private IUserService userService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private MyOrderMapper myOrderMapper;

    /**
     * 生成订单操作
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<String> generateOrder(OrderDTO orderDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 判断是否选择购买商品
        List<String> cartIdList = orderDTO.getCartList();
        if(cartIdList == null || cartIdList.size() == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CART_SELECT_EMPTY);
        }
        // 验证商品库存
        for(String id : cartIdList) {
            Cart cart = cartMapper.selectByPrimaryKey(id);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product == null) {
                CodeMsg codeMsg = CodeMsg.PRODUCT_NOT_EXIST;
                String msg = "商品《" + product.getName() + "》不存在哦！";
                codeMsg.setMsg(msg);
                return ResponseDTO.errorByMsg(codeMsg);
            }
            if(product.getStock() < cart.getQuantity()) {
                CodeMsg codeMsg = CodeMsg.PRODUCT_STOCK_EMPTY;
                String msg = "商品《" + product.getName() + "》库存不足哦！";
                codeMsg.setMsg(msg);
                return ResponseDTO.errorByMsg(codeMsg);
            }
        }
        BigDecimal totalPrice = new BigDecimal(0.00);
        String orderId = UuidUtil.getShortUuid();
        // 订单详情信息封装并落库
        for(String id : cartIdList) {
            Cart cart = cartMapper.selectByPrimaryKey(id);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UuidUtil.getShortUuid());
            orderItem.setProductName(product.getName());
            if(DiscountEnum.YES.getCode().equals(product.getIsDiscount())) {
                // 进行打折
                orderItem.setProductPrice(product.getNewPrice());
                orderItem.setSumPrice(product.getNewPrice().multiply(new BigDecimal(cart.getQuantity())));
            } else {
                // 没有进行打折
                orderItem.setProductPrice(product.getPrice());
                orderItem.setSumPrice(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            }
            orderItem.setProductPhoto(product.getPhoto());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setProductId(product.getId());
            orderItem.setOrderId(orderId);
            totalPrice = totalPrice.add(orderItem.getSumPrice());
            if(orderItemMapper.insertSelective(orderItem) == 0) {
                throw new RuntimeException("生成订单失败，请稍后重试！");
            }
            // 扣商品库存
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        Order order = CopyUtil.copy(orderDTO, Order.class);
        order.setNo(String.valueOf(new SnowFlake(2,3).nextId()));
        order.setUserId(responseDTO.getData().getId());
        order.setCreateTime(new Date());
        order.setId(orderId);
        order.setState(OrderStateEnum.UN_PAY.getCode());
        order.setTotalPrice(totalPrice);
        if(orderMapper.insertSelective(order) == 0) {
            throw new RuntimeException("生成订单失败，请稍后重试！");
        }
        return ResponseDTO.success(orderId);
    }

    /**
     * 获取订单信息
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<OrderDTO> getOrder(OrderDTO orderDTO) {
        if(CommonUtil.isEmpty(orderDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 获取订单信息
        Order order = orderMapper.selectByPrimaryKey(orderDTO.getId());
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
        // 获取订单详情信息
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        orderDTO = CopyUtil.copy(order, OrderDTO.class);
        orderDTO.setOrderItemDTOList(CopyUtil.copyList(orderItemList, OrderItemDTO.class));
        return ResponseDTO.success(orderDTO);
    }

    /**
     * 更改订单信息操作
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateOrder(OrderDTO orderDTO) {
        if(CommonUtil.isEmpty(orderDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        Order order = CopyUtil.copy(orderDTO, Order.class);
        // 修改订单信息
        if(orderMapper.updateByPrimaryKeySelective(order) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_UPDATE_ERROR);
        }
        return ResponseDTO.success(true);
    }

    /**
     * 支付订单操作
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> payOrder(OrderDTO orderDTO) {
        if(CommonUtil.isEmpty(orderDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        // 判断是否填写地址
        if(CommonUtil.isEmpty(orderDTO.getAddressId())) {
            return  ResponseDTO.errorByMsg(CodeMsg.ADDRESS_EMPTY);
        }
        Order order = CopyUtil.copy(orderDTO, Order.class);
        Address address = addressMapper.selectByPrimaryKey(orderDTO.getAddressId());
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(address.getReceiverAddress());
        order.setState(OrderStateEnum.PAYED.getCode());
        // 修改订单信息
        if(orderMapper.updateByPrimaryKeySelective(order) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_UPDATE_ERROR);
        }
        // 增加商品销量
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        for(OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setSellNum(product.getSellNum() + orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
            // 清除购物车数据
            CartExample cartExample = new CartExample();
            cartExample.createCriteria().andUserIdEqualTo(responseDTO.getData().getId()).andProductIdEqualTo(orderItem.getProductId());
            cartMapper.deleteByExample(cartExample);
        }
        return ResponseDTO.successByMsg(true, "支付订单成功！");
    }

    /**
     * 获取所有订单信息
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<List<OrderDTO>> getOrderList(OrderDTO orderDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        OrderExample orderExample = new OrderExample();
        if(orderDTO.getState() == null || orderDTO.getState() == 0) {
            orderExample.createCriteria().andUserIdEqualTo(responseDTO.getData().getId());
        } else {
            orderExample.createCriteria().andUserIdEqualTo(responseDTO.getData().getId()).andStateEqualTo(orderDTO.getState());
        }
        orderExample.setOrderByClause("create_time desc");
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        List<OrderDTO> orderDTOList = CopyUtil.copyList(orderList, OrderDTO.class);
        for(OrderDTO o : orderDTOList) {
            OrderItemExample orderItemExample = new OrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(o.getId());
            List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            o.setOrderItemDTOList(CopyUtil.copyList(orderItemList, OrderItemDTO.class));
        }
        return ResponseDTO.success(orderDTOList);
    }

    /**
     * 取消订单操作
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> cancelOrder(OrderDTO orderDTO) {
        // 获取当前登录用户
        UserDTO userDTO = new UserDTO();
        userDTO.setToken(orderDTO.getToken());
        ResponseDTO<UserDTO> responseDTO = userService.checkLogin(userDTO);
        if(responseDTO.getCode() != 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        Order order = CopyUtil.copy(orderDTO, Order.class);
        // 修改订单状态
        order.setState(OrderStateEnum.CANCEL.getCode());
        if(orderMapper.updateByPrimaryKeySelective(order) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_CANCEL_ERROR);
        }
        // 获取订单详情信息
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        for(OrderItem orderItem : orderItemList) {
            // 恢复商品库存
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            if(product == null) {
                continue;
            }
            product.setStock(product.getStock() + orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
        return ResponseDTO.successByMsg(true, "取消订单成功！");
    }


    /**
     * 修改订单状态操作
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> updateOrderState(OrderDTO orderDTO) {
        if(orderDTO.getState() == null || CommonUtil.isEmpty(orderDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Order order = orderMapper.selectByPrimaryKey(orderDTO.getId());
        if(OrderStateEnum.CANCEL.getCode().equals(orderDTO.getState())){
            return cancelOrder(orderDTO);
        }
        if(OrderStateEnum.PAYED.getCode().equals(orderDTO.getState())){
            return payOrder(orderDTO);
        }
        if(OrderStateEnum.UN_PAY.getCode().equals(orderDTO.getState())){
            // 获取订单详情信息
            OrderItemExample orderItemExample = new OrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(order.getId());
            List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            for(OrderItem orderItem : orderItemList) {
                // 恢复商品库存
                Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
                if(product == null) {
                    continue;
                }
                product.setStock(product.getStock() + orderItem.getQuantity());
                productMapper.updateByPrimaryKeySelective(product);
            }
        }
        // 修改订单状态
        order.setState(orderDTO.getState());
        if(orderMapper.updateByPrimaryKeySelective(order) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_UPDATE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "成功修改订单状态！");
    }

    /**
     * 分页获取订单数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<OrderDTO>> getOrderListByPage(PageDTO<OrderDTO> pageDTO) {
        OrderExample orderExample = new OrderExample();
        // 判断是否进行关键字搜索
        if(!CommonUtil.isEmpty(pageDTO.getSearchContent())){
            orderExample.createCriteria().andNoLike("%"+pageDTO.getSearchContent()+"%");
        }
        orderExample.setOrderByClause("create_time desc");
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        pageDTO.setSize(5);
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出订单数据
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<OrderDTO> orderDTOList = CopyUtil.copyList(orderList, OrderDTO.class);
        for(OrderDTO orderDTO : orderDTOList){
            User user = userMapper.selectByPrimaryKey(orderDTO.getUserId());
            orderDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(orderDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 后台删除订单数据
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeOrder(OrderDTO orderDTO) {
        if(CommonUtil.isEmpty(orderDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除订单数据
        if(orderMapper.deleteByPrimaryKey(orderDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_DELETE_ERROR);
        }
        // 删除订单详情数据
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(orderDTO.getId());
        if(orderItemMapper.deleteByExample(orderItemExample) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.ORDER_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功");
    }

    /**
     * 根据订单id获取订单详情信息
     * @param orderDTO
     * @return
     */
    @Override
    public ResponseDTO<OrderDTO> getOrderItemByOrderId(OrderDTO orderDTO) {
        if(CommonUtil.isEmpty(orderDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Order order = orderMapper.selectByPrimaryKey(orderDTO.getId());
        orderDTO = CopyUtil.copy(order, OrderDTO.class);
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(orderDTO.getId());
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
        List<OrderItemDTO> orderItemDTOList = CopyUtil.copyList(orderItemList, OrderItemDTO.class);
        orderDTO.setOrderItemDTOList(orderItemDTOList);
        return ResponseDTO.success(orderDTO);
    }

    /**
     * 获取订单总数
     * @return
     */
    @Override
    public ResponseDTO<Integer> getOrderTotal() {
        int total = orderMapper.countByExample(new OrderExample());
        return ResponseDTO.success(total);
    }

    /**
     * 获取今天订单成交金额
     * @return
     */
    @Override
    public ResponseDTO<BigDecimal> getTodayPrice() {
        return ResponseDTO.success(myOrderMapper.todayTotalPrice());
    }

    /**
     * 获取本周订单成交金额
     * @return
     */
    @Override
    public ResponseDTO<BigDecimal> getWeekPrice() {
        return ResponseDTO.success(myOrderMapper.weekTotalPrice());
    }

    /**
     * 获取本月订单成交金额
     * @return
     */
    @Override
    public ResponseDTO<BigDecimal> getMonthPrice() {
        return ResponseDTO.success(myOrderMapper.monthTotalPrice());
    }

    @Override
    public ResponseDTO<List<Integer>> getOrderCountByDateAndState() {
        List<Integer> totalList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        List<Integer> finishStateList = new ArrayList<>();
        finishStateList.add(2);
        finishStateList.add(3);
        finishStateList.add(4);
        List<Integer> failStateList = new ArrayList<>();
        failStateList.add(1);
        failStateList.add(5);
        // 获取当天已完成的收益次数
        queryMap.put("start", 0);
        queryMap.put("end", -1);
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, finishStateList));
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, failStateList));
        // 获取昨天已完成的收益次数
        queryMap.put("start", 1);
        queryMap.put("end", 0);
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, finishStateList));
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, failStateList));
        // 获取前天已完成的收益次数
        queryMap.put("start", 2);
        queryMap.put("end", 1);
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, finishStateList));
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, failStateList));
        // 获取大前天已完成的收益次数
        queryMap.put("start", 3);
        queryMap.put("end", 2);
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, finishStateList));
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, failStateList));
        // 获取大大前天已完成的收益次数
        queryMap.put("start", 4);
        queryMap.put("end", 3);
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, finishStateList));
        totalList.add(myOrderMapper.getOrderTotalByDateAndState(queryMap, failStateList));
        return ResponseDTO.success(totalList);
    }

}
