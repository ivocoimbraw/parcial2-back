package com.example.demo.util;

import com.example.demo.user.dto.UserProfileDTO;
import com.example.demo.user.dto.UserView;

public class UserUtil {
    
    public static UserProfileDTO toUser(UserView userView) {
        return new UserProfileDTO(userView.getEmail(), userView.getName());
    }

}
