package com.ks.management.security.controller;

import com.ks.management.security.User;
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
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Integer userId){
        return userService.getOne(userId);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable("userId") Integer userId, @RequestBody User user, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(!userId.equals(user.getId())){
            throw new RuntimeException("Id in path does not match request body");
        }
        return  userService.updateUser(user,userPrincipal);
    }

}
