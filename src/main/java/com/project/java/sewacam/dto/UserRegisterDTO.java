package com.project.java.sewacam.dto;

import com.project.java.sewacam.model.User;
import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private User.Role role;
}
