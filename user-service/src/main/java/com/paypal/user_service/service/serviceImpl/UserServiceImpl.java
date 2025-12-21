package com.paypal.user_service.service.serviceImpl;

import com.paypal.user_service.dto.CreateWalletRequest;
import com.paypal.user_service.entity.User;
import com.paypal.user_service.repository.UserRepository;
import com.paypal.user_service.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(User user) {
        User savedUser =  userRepository.save(user);
        try {
            CreateWalletRequest createWalletRequest = new CreateWalletRequest();
            createWalletRequest.setUserId(savedUser.getId());
            createWalletRequest.setCurrency("INR");

        } catch (Exception e) {
            userRepository.deleteById(savedUser.getId());
            throw new RuntimeException("Error while creating wallet for user: " + savedUser.getId(), e);
        }
        return savedUser;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
