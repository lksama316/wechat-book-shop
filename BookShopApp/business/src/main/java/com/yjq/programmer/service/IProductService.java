package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-05 20:13
 */
public interface IProductService {

    // 分页获取商品数据
    ResponseDTO<PageDTO<ProductDTO>> getProductListByPage(PageDTO<ProductDTO> pageDTO);

    // 保存商品数据(添加、修改)
    ResponseDTO<Boolean> saveProduct(ProductDTO productDTO);

    // 删除商品数据
    ResponseDTO<Boolean> removeProduct(ProductDTO productDTO);

    // 前台获取热销商品数据
    ResponseDTO<List<ProductDTO>> getHotProductList();

    // 前台获取新品商品数据
    ResponseDTO<List<ProductDTO>> getNewProductList();

    // 前台获取折扣商品数据
    ResponseDTO<List<ProductDTO>> getDiscountProductList();

    // 前台获取全部商品数据
    ResponseDTO<List<ProductDTO>> getAllProductList(ProductDTO productDTO);

    // 前台获取商品详情数据
    ResponseDTO<ProductDTO> getProductInfo(ProductDTO productDTO);

    // 获取商品总数
    ResponseDTO<Integer> getProductTotal();
}
