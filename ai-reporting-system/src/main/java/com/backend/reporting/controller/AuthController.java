package com.backend.reporting.controller;

import com.backend.reporting.config.JwtUtil;
import com.backend.reporting.dto.ApiResponse;
import com.backend.reporting.dto.LoginRequest;
import com.backend.reporting.entity.Role;
import com.backend.reporting.entity.User;
import com.backend.reporting.exception.UnauthorizedException;
import com.backend.reporting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return "User registered successfully";
    }
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return ApiResponse.success(token);
    }



}
