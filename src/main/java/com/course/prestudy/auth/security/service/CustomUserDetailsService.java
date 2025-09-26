package com.course.prestudy.auth.security.service;

import com.course.prestudy.domain.user.entity.User;
import com.course.prestudy.domain.user.exception.UserException;
import com.course.prestudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UserException.NotFoundException(username));
    }

    private UserDetails createUserDetails(User domainUser) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(domainUser.getUserRole().getKey());

        return org.springframework.security.core.userdetails.User.builder()
                .username(domainUser.getUsername())
                .password(domainUser.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }
}
