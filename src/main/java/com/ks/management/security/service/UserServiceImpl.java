package com.ks.management.security.service;

import com.ks.management.employee.Employee;
import com.ks.management.security.*;
import com.ks.management.security.dao.UserEmployeeJpa;
import com.ks.management.security.ui.UserEmployeeLinkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserEmployeeJpa userEmployeeJpa;

    @Override
    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll();

        final List<UserDTO> dtos = users.stream()
                .map(user -> {
                    return UserDTO.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .isActive(user.getIsActive())
                            .roles(user.getRoles())
                            .updatedDate(user.getUpdatedDate())
                            .createdDate(user.getCreatedDate())
                            .build();
                })
                .collect(Collectors.toList());

        return dtos;
    }

    @Override
    public UserDTO getOne(Integer userId) {
        final User user = userRepository.getOne(userId);
        final UserDTO userDto = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .roles(user.getRoles())
                .updatedDate(user.getUpdatedDate())
                .createdDate(user.getCreatedDate())
                .build();
        return userDto;
    }

    @Override
    public UserDTO updateUser(UserDTO userDto, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);

        final String username = Optional.ofNullable(userDto.getUsername()).orElse("");
        final Boolean isActive = Optional.ofNullable(userDto.getIsActive()).orElse(null);
        final String roles = Optional.ofNullable(userDto.getRoles()).orElse("");

        final User user = userRepository.getOne(userDto.getId());
        user.setUpdatedBy(activeUserId);
        if(!username.isEmpty()){
            user.setUsername(username);
        }
        if(isActive != null){
            user.setIsActive(isActive);
        }
        if(!roles.isEmpty()){
            user.setRoles(roles);
        }
        final User updatedUser = userRepository.save(user);

        final UserDTO updatedUserDto = UserDTO.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .isActive(updatedUser.getIsActive())
                .roles(updatedUser.getRoles())
                .updatedDate(updatedUser.getUpdatedDate())
                .createdDate(updatedUser.getCreatedDate())
                .build();

        return updatedUserDto;
    }

    @Override
    public UserDTO updateUserPassword(UserDTO userDTO, UserPrincipal userPrincipal) {
        final Integer activeUserId = userPrincipal.getUserId();

        final User user = userRepository.getOne(userDTO.getId());

        final String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setUpdatedBy(activeUserId);

        final User updatedUser = userRepository.save(user);

        UserDTO updatedUserDto = null;
        if(updatedUser != null){
            updatedUserDto = UserDTO.builder()
                    .id(updatedUser.getId())
                    .username(updatedUser.getUsername())
                    .isActive(updatedUser.getIsActive())
                    .roles(updatedUser.getRoles())
                    .updatedDate(updatedUser.getUpdatedDate())
                    .createdDate(updatedUser.getCreatedDate())
                    .build();
        }
        return updatedUserDto;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO, UserPrincipal userPrincipal) {
        if(!userDTO.getPassword().equalsIgnoreCase(userDTO.getConfirmPassword())){
            throw new RuntimeException("Passwords must match!");
        }
        final String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());

        final User user = User.builder()
                .username(userDTO.getUsername())
                .password(encodedPassword)
                .isActive(userDTO.getIsActive())
                .roles(userDTO.getRoles())
                .updatedBy(userPrincipal.getUserId())
                .createdBy(userPrincipal.getUserId())
                .build();

        final User savedUser = userRepository.save(user);
        final UserDTO newUserDto = UserDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .isActive(savedUser.getIsActive())
                .roles(savedUser.getRoles())
                .updatedDate(savedUser.getUpdatedDate())
                .createdDate(savedUser.getCreatedDate())
                .build();
        return newUserDto;
    }
    @Override
    public UserEmployee createUserEmployee(UserEmployeeLinkDto userEmployeeLinkDto, UserPrincipal userPrincipal) {
        final Integer activeUser = userPrincipal.getUserId();
        final UserEmployee userEmployee = UserEmployee.builder()
                .user(userEmployeeLinkDto.getUser())
                .employee(userEmployeeLinkDto.getEmployee())
                .createdBy(activeUser)
                .updatedBy(activeUser)
                .build();

        return userEmployeeJpa.save(userEmployee);
    }
}
