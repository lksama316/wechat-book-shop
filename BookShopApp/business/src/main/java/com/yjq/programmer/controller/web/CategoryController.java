package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-06 14:56
 */
@RestController("WebCategoryController")
@RequestMapping("/web/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    /**
     * 前台获取商品分类列表数据
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<List<CategoryDTO>> list(){
        return categoryService.list();
    }
}
