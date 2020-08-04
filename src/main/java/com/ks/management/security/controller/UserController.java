package com.ks.management.security.controller;

import com.ks.management.security.User;
import com.ks.management.security.UserDTO;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.UserRepository;
import com.ks.management.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public List<UserDTO> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable("userId") Integer userId){
        return userService.getOne(userId);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable("userId") Integer userId, @RequestBody UserDTO user, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(!userId.equals(user.getId())){
            throw new RuntimeException("Id in path does not match request body");
        }
        return  userService.updateUser(user,userPrincipal);
    }

    @PutMapping("/{userId}/password")
    public UserDTO updateUserPassword(@PathVariable("userId")Integer userId, @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(!userId.equals(userDTO.getId()) || userDTO.getPassword().isEmpty() ){
            throw new RuntimeException("Bad Request");
        }
        return userService.updateUserPassword(userDTO,userPrincipal);
    }



}
