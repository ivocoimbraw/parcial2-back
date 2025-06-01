package com.example.demo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.user.dto.UserView;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }

    public UserView findUserViewByUsername(String username) {
        return userRepository.findUserViewByEmail(username).orElse(null);
    }

    public UserView findUserProfileDTO() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserView userView = findUserViewByUsername(userDetails.getUsername());
        return userView;
    }

}
