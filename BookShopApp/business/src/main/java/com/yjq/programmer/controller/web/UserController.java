package com.yjq.programmer.controller.web;

import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-12-28 21:44
 */
@RestController("WebUserController")
@RequestMapping("/web/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 用户注册操作
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseDTO<Boolean> registerUser(@RequestBody UserDTO userDTO){
        return userService.registerUser(userDTO);
    }

    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> webLogin(@RequestBody UserDTO userDTO){
        return userService.webLogin(userDTO);
    }

    /**
     * 用户退出登录操作
     * @param userDTO
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }

    /**
     * 获取用户信息操作
     * @param userDTO
     * @return
     */
    @PostMapping("/get")
    public ResponseDTO<UserDTO> getUser(@RequestBody UserDTO userDTO){
        return userService.getUser(userDTO);
    }

    /**
     * 修改个人信息操作
     * @param userDTO
     * @return
     */
    @PostMapping("/update")
    public ResponseDTO<Boolean> updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }
}
