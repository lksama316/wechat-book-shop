package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IProductService;
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
 * @create 2022-05-06 16:40
 */
@RestController("WebProductController")
@RequestMapping("/web/product")
public class ProductController {

    @Resource
    private IProductService productService;

    /**
     * 前台获取热销商品数据
     * @return
     */
    @PostMapping("/hot")
    public ResponseDTO<List<ProductDTO>> getHotProductList(){
        return productService.getHotProductList();
    }

    /**
     * 前台获取新品商品数据
     * @return
     */
    @PostMapping("/new")
    public ResponseDTO<List<ProductDTO>> getNewProductList(){
        return productService.getNewProductList();
    }

    /**
     * 前台获取折扣商品数据
     * @return
     */
    @PostMapping("/discount")
    public ResponseDTO<List<ProductDTO>> getDiscountProductList(){
        return productService.getDiscountProductList();
    }

    /**
     * 前台获取商品列表数据
     * @param productDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<ProductDTO>> getAllProductList(@RequestBody ProductDTO productDTO){
        return productService.getAllProductList(productDTO);
    }

    /**
     * 前台获取商品详情数据
     * @param productDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<ProductDTO> getProductInfo(@RequestBody ProductDTO productDTO){
        return productService.getProductInfo(productDTO);
    }

}
