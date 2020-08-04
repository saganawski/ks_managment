package com.ks.management.security.service;

import com.ks.management.security.UserDTO;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO getOne(Integer userId);

    UserDTO updateUser(UserDTO user, UserPrincipal userPrincipal);

    UserDTO updateUserPassword(UserDTO userDTO, UserPrincipal userPrincipal);
}
