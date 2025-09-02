/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.java.sewacam.service;

import com.project.java.sewacam.model.User;
import com.project.java.sewacam.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hilal
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsersByRole(String roleStr) {
        User.Role role = toRole(roleStr);
        return userRepository.findByRole(role);
    }

    private User.Role toRole(String s) {
        if (s == null) {
            throw new IllegalArgumentException("role is required");
        }
        try {
            return User.Role.valueOf(s.trim().toLowerCase()); // "renter" -> RENTER
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role: " + s + " (allowed: owner, renter)");
        }
    }
}
