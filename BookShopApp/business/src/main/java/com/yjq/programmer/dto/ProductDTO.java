package com.yjq.programmer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-05 19:57
 */
public class ProductDTO {
    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="商品名称不能为空！",errorMaxLengthMsg="商品名称长度不能大于16！",errorMinLengthMsg="商品名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=999999.99,minValue=0.00,errorRequiredMsg="商品价格不能为空！",errorMaxValueMsg="商品价格不能大于999999.99元！",errorMinValueMsg="商品价格不能小于0.00元！")
    private BigDecimal price;

    private Integer isDiscount;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=999999.99,minValue=0.00,errorRequiredMsg="商品折扣价不能为空！",errorMaxValueMsg="商品折扣价不能大于999999.99元！",errorMinValueMsg="商品折扣价不能小于0.00元！")
    private BigDecimal newPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="出版社不能为空！",errorMaxLengthMsg="出版社长度不能大于16！",errorMinLengthMsg="出版社不能为空！")
    private String press;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="图书作者不能为空！",errorMaxLengthMsg="图书作者长度不能大于16！",errorMinLengthMsg="图书作者不能为空！")
    private String author;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="图书字数不能为空！",errorMaxLengthMsg="图书字数长度不能大于8！",errorMinLengthMsg="图书字数不能为空！")
    private String wordNum;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999,minValue=0,errorRequiredMsg="商品库存不能为空！",errorMaxValueMsg="商品库存不能大于99999999！",errorMinValueMsg="商品库存不能为小于0！")
    private Integer stock;

    private String photo;

    @ValidateEntity(required=true,requiredMinLength=true,minLength=1,errorRequiredMsg="商品所属分类不能为空！",errorMinLengthMsg="商品所属分类不能为空！")
    private String categoryId;

    private CategoryDTO categoryDTO;

    private Integer sellNum;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="商品重量不能为空！",errorMaxLengthMsg="商品重量长度不能大于8！",errorMinLengthMsg="商品重量不能为空！")
    private String weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWordNum() {
        return wordNum;
    }

    public void setWordNum(String wordNum) {
        this.wordNum = wordNum;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", price=").append(price);
        sb.append(", isDiscount=").append(isDiscount);
        sb.append(", newPrice=").append(newPrice);
        sb.append(", createTime=").append(createTime);
        sb.append(", press=").append(press);
        sb.append(", author=").append(author);
        sb.append(", wordNum=").append(wordNum);
        sb.append(", stock=").append(stock);
        sb.append(", photo=").append(photo);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", categoryDTO=").append(categoryDTO);
        sb.append(", sellNum=").append(sellNum);
        sb.append(", weight=").append(weight);
        sb.append("]");
        return sb.toString();
    }
}
