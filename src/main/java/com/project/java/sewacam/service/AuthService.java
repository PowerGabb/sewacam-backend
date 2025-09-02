package com.project.java.sewacam.service;

import com.project.java.sewacam.dto.UserLoginDTO;
import com.project.java.sewacam.dto.UserRegisterDTO;
import com.project.java.sewacam.model.User;
import com.project.java.sewacam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegisterDTO registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPhoneNumber(registerDto.getPhoneNumber());
        newUser.setAddress(registerDto.getAddress());
        newUser.setRole(registerDto.getRole());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        return userRepository.save(newUser);
    }

    public Optional<User> authenticate(UserLoginDTO loginDto) {
        Optional<User> userOptional = userRepository.findByEmail(loginDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return userOptional;
            }
        }
        return Optional.empty();
    }
}