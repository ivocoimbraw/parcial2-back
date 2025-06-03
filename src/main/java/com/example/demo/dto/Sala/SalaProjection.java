package com.example.demo.dto.Sala;

import java.util.List;

import com.example.demo.user.dto.UserView;

public interface SalaProjection {
    Integer getId();
    String name();
    UserView getOwner();
    String getDatosJson();
    List<UserView> getUsers();
}
