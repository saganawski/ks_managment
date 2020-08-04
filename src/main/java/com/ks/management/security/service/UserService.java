package com.ks.management.security.service;

import com.ks.management.security.User;
import com.ks.management.security.UserDTO;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User getOne(Integer userId);

    User updateUser(User user, UserPrincipal userPrincipal);

    UserDTO updateUserPassword(UserDTO userDTO, UserPrincipal userPrincipal);
}
