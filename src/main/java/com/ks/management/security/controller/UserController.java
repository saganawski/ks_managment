package com.ks.management.security.controller;

import com.ks.management.security.User;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
