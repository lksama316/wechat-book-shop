package com.yjq.programmer.service;

import com.yjq.programmer.dto.CategoryDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-05 19:40
 */
public interface ICategoryService {

    // 分页获取商品分类数据
    ResponseDTO<PageDTO<CategoryDTO>> getCategoryListByPage(PageDTO<CategoryDTO> pageDTO);

    // 保存商品分类数据(添加、修改)
    ResponseDTO<Boolean> saveCategory(CategoryDTO categoryDTO);

    // 删除商品分类数据
    ResponseDTO<Boolean> removeCategory(CategoryDTO categoryDTO);

    // 获取所有商品分类数据
    ResponseDTO<List<CategoryDTO>> getAllCategoryList();

    // 前台获取商品分类列表数据
    ResponseDTO<List<CategoryDTO>> list();

    // 获取五个成交额最高的商品分类
    ResponseDTO<List<CategoryDTO>> getCategoryListByPrice();
}
