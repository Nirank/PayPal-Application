package com.paypal.user_service.service;

import com.paypal.user_service.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);
}
