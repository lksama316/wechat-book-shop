package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-05-10 8:40
 */
public class AddressDTO {
    private String id;

    private String userId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="收货人姓名不能为空！",errorMaxLengthMsg="收货人姓名长度不能大于8！",errorMinLengthMsg="收货人姓名不能为空！")
    private String receiverName;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="收货人电话不能为空！",errorMaxLengthMsg="请输入11位收货人电话！",errorMinLengthMsg="请输入11位收货人电话！")
    private String receiverPhone;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="收货人地址不能为空！",errorMaxLengthMsg="收货人地址长度不能大于32！",errorMinLengthMsg="收货人地址不能为空！")
    private String receiverAddress;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", receiverName=").append(receiverName);
        sb.append(", receiverPhone=").append(receiverPhone);
        sb.append(", receiverAddress=").append(receiverAddress);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}
