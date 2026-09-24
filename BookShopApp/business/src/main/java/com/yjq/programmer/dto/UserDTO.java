package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-28 21:43
 */
public class UserDTO {
    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="用户昵称不能为空！",errorMaxLengthMsg="用户昵称长度不能大于8！",errorMinLengthMsg="用户昵称不能为空！")
    private String username;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="用户密码不能为空！",errorMaxLengthMsg="用户密码长度不能大于16！",errorMinLengthMsg="用户密码长度不能小于6！")
    private String password;

    private String rePassword;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="手机号码不能为空！",errorMaxLengthMsg="请输入11位手机号码！",errorMinLengthMsg="请输入11位手机号码！")
    private String phone;

    private Integer sex;

    private Integer roleId;

    private String headPic;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", phone=").append(phone);
        sb.append(", sex=").append(sex);
        sb.append(", roleId=").append(roleId);
        sb.append(", headPic=").append(headPic);
        sb.append(", rePassword=").append(rePassword);
        sb.append(", token=").append(token);
        sb.append("]");
        return sb.toString();
    }
}
