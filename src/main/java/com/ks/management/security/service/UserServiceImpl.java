package com.ks.management.security.service;

import com.ks.management.security.User;
import com.ks.management.security.UserDTO;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getOne(Integer userId) {
        return userRepository.getOne(userId);
    }

    @Override
    public User updateUser(User user, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        user.setUpdatedBy(activeUserId);
        return userRepository.save(user);
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
                    .build();
        }
        return updatedUserDto;
    }
}
