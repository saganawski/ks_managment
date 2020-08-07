package com.ks.management.security.service;

import com.ks.management.employee.Employee;
import com.ks.management.security.User;
import com.ks.management.security.UserDTO;
import com.ks.management.security.UserEmployee;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.ui.UserEmployeeLinkDto;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO getOne(Integer userId);

    UserDTO updateUser(UserDTO user, UserPrincipal userPrincipal);

    UserDTO updateUserPassword(UserDTO userDTO, UserPrincipal userPrincipal);

    UserDTO createUser(UserDTO userDTO, UserPrincipal userPrincipal);

    UserEmployee createUserEmployee(UserEmployeeLinkDto userEmployeeLinkDto, UserPrincipal userPrincipal);

    Employee getEmployeeForUserById(Integer userId);
}
