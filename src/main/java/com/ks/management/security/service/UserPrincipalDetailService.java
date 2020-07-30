package com.ks.management.security.service;

import com.ks.management.security.User;
import com.ks.management.security.UserPrincipal;
import com.ks.management.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = this.userRepository.findByUsername(s);
        final UserPrincipal userPrincipal = new UserPrincipal(user);

        return userPrincipal;
    }
}
