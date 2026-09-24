package com.yjq.programmer.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private String id;

    private String name;

    private BigDecimal price;

    private Integer isDiscount;

    private BigDecimal newPrice;

    private Date createTime;

    private String press;

    private String author;

    private String wordNum;

    private Integer stock;

    private String photo;

    private String categoryId;

    private Integer sellNum;

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
        sb.append(", sellNum=").append(sellNum);
        sb.append(", weight=").append(weight);
        sb.append("]");
        return sb.toString();
    }
}