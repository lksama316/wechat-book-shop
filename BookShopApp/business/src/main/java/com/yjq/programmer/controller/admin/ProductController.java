package com.yjq.programmer.controller.admin;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ProductDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-05 20:17
 */
@RestController("AdminProductController")
@RequestMapping("/admin/product")
public class ProductController {

    @Resource
    private IProductService productService;

    /**
     * 后台保存商品数据(添加、修改)
     * @param productDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveProduct(@RequestBody ProductDTO productDTO){
        return productService.saveProduct(productDTO);
    }

    /**
     * 后台分页获取商品数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ProductDTO>> getProductListByPage(@RequestBody PageDTO<ProductDTO> pageDTO){
        return productService.getProductListByPage(pageDTO);
    }

    /**
     * 后台删除商品数据
     * @param productDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeProduct(@RequestBody ProductDTO productDTO){
        return productService.removeProduct(productDTO);
    }

    /**
     * 后台获取商品总数
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Integer> getProductTotal(){
        return productService.getProductTotal();
    }

}
