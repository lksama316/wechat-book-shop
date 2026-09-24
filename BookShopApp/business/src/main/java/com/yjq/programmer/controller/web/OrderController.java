package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.OrderDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IOrderService;
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
 * @create 2022-05-09 17:05
 */
@RestController("WebOrderController")
@RequestMapping("/web/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 生成订单操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/generate")
    public ResponseDTO<String> generateOrder(@RequestBody OrderDTO orderDTO){
        return orderService.generateOrder(orderDTO);
    }

    /**
     * 获取订单信息
     * @param orderDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<OrderDTO> getOrder(@RequestBody OrderDTO orderDTO){
        return orderService.getOrder(orderDTO);
    }

    /**
     * 获取所有订单信息
     * @param orderDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<OrderDTO>> getOrderList(@RequestBody OrderDTO orderDTO){
        return orderService.getOrderList(orderDTO);
    }

    /**
     * 更改订单信息操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseDTO<Boolean> updateOrder(@RequestBody OrderDTO orderDTO){
        return orderService.updateOrder(orderDTO);
    }


    /**
     * 取消订单信息操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/cancel")
    public ResponseDTO<Boolean> cancelOrder(@RequestBody OrderDTO orderDTO){
        return orderService.cancelOrder(orderDTO);
    }

    /**
     * 支付订单操作
     * @param orderDTO
     * @return
     */
    @PostMapping("/pay")
    public ResponseDTO<Boolean> payOrder(@RequestBody OrderDTO orderDTO){
        return orderService.payOrder(orderDTO);
    }
}
