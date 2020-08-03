package com.ks.management.security.service;

import com.ks.management.security.User;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
