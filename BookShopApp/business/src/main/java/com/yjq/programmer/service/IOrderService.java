package com.yjq.programmer.service;

import com.yjq.programmer.dto.OrderDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-09 17:06
 */
public interface IOrderService {

    // 生成订单操作
    ResponseDTO<String> generateOrder(OrderDTO orderDTO);

    // 获取订单信息
    ResponseDTO<OrderDTO> getOrder(OrderDTO orderDTO);

    // 更改订单信息操作
    ResponseDTO<Boolean> updateOrder(OrderDTO orderDTO);

    // 支付订单操作
    ResponseDTO<Boolean> payOrder(OrderDTO orderDTO);

    // 获取所有订单信息
    ResponseDTO<List<OrderDTO>> getOrderList(OrderDTO orderDTO);

    // 取消订单操作
    ResponseDTO<Boolean> cancelOrder(OrderDTO orderDTO);

    // 修改订单状态操作
    ResponseDTO<Boolean> updateOrderState(OrderDTO orderDTO);

    // 分页获取订单数据
    ResponseDTO<PageDTO<OrderDTO>> getOrderListByPage(PageDTO<OrderDTO> pageDTO);

    // 后台删除订单数据
    ResponseDTO<Boolean> removeOrder(OrderDTO orderDTO);

    // 根据订单id获取订单详情信息
    ResponseDTO<OrderDTO> getOrderItemByOrderId(OrderDTO orderDTO);

    // 获取订单总数
    ResponseDTO<Integer> getOrderTotal();

    // 获取今天订单成交金额
    ResponseDTO<BigDecimal> getTodayPrice();

    // 获取本周订单成交金额
    ResponseDTO<BigDecimal> getWeekPrice();

    // 获取本月订单成交金额
    ResponseDTO<BigDecimal> getMonthPrice();

    // 根据时间范围和订单状态获取交易的订单总数
    ResponseDTO<List<Integer>> getOrderCountByDateAndState();
}
