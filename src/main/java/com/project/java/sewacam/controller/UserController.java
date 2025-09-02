package com.project.java.sewacam.controller;

import com.project.java.sewacam.dto.LoginResponseDTO;
import com.project.java.sewacam.dto.UserLoginDTO;
import com.project.java.sewacam.dto.UserRegisterDTO;
import com.project.java.sewacam.model.User;
import com.project.java.sewacam.service.AdminService;
import com.project.java.sewacam.service.AuthService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO registerDto) {
        try {
            User registeredUser = authService.registerUser(registerDto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody UserLoginDTO loginDto) {
        Optional<User> userOptional = authService.authenticate(loginDto);
        if (userOptional.isPresent()) {
            User u = userOptional.get();
            // Dalam aplikasi nyata, kembalikan token JWT di sini
            String token = "random-jwt";
            System.out.println("DEBUG user id = " + u.getId());            
            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            "Login successful for user: " + u.getUsername(),
                            token,
                            u.getUsername(),
                            u.getEmail(),
                            u.getPhoneNumber() + "",
                            u.getAddress() + "",
                            u.getRole() + "",
                            u.getId()
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO("Invalid credentials", null, null, null, null, null, null, 0));
        }
    }
    
  
    
}
