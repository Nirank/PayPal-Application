package com.paypal.user_service.controller;


import com.paypal.user_service.dto.JwtTokenResponse;
import com.paypal.user_service.dto.LoginRequest;
import com.paypal.user_service.dto.SignInRequest;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserService;
import com.paypal.user_service.utils.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtils jwtUtils;

    private final UserService userService;


    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, UserService userService) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignInRequest request){
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole("ROLE_USER");  // Normal users only!
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save the new user and create a Wallet
        User savedUser = userService.createUser(user);
                //userRepository.save(user);



        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate token with claims
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
