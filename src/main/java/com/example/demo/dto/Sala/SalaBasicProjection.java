package com.example.demo.dto.Sala;

import com.example.demo.user.dto.UserView;

public interface SalaBasicProjection {
    Integer getId();
    String getName();
    UserView getOwner();
}
