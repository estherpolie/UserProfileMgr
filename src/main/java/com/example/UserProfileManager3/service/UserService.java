package com.example.UserProfileManager3.service;

import com.example.UserProfileManager3.Exception.NotFoundException;
import com.example.UserProfileManager3.entity.User;
import com.example.UserProfileManager3.filter.JwtUtil;
import com.example.UserProfileManager3.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.example.UserProfileManager3.Exception.NotFoundException;
import com.example.UserProfileManager3.entity.User;
import com.example.UserProfileManager3.filter.JwtUtil;
import com.example.UserProfileManager3.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }
    @Autowired
    private ObjectMapper objectMapper;

    public User save(User user) throws JsonProcessingException {
        User savedUser = userRepository.save(user);
        String logMessage = objectMapper.writeValueAsString(savedUser);
        logger.info("Saved user: {}", logMessage);
        logAuditTrail("User saved successfully", savedUser.getId());
        return savedUser;
    }

    public List<User> getAllUsers()//list all Users
    {
        List<User> allUsers =userRepository.findAll();
        logger.info("Retrieved all users: {}", allUsers.size());

        return allUsers;
    }

    public Optional<User> findById(Long id)//find user by id
    {
        Optional<User> user = userRepository.findById(id);
            user.ifPresent(u -> {
                try {
                    String logMessage = objectMapper.writeValueAsString(u);
                    logger.info("Retrieved user by id {}: {}", id, logMessage);
                    logAuditTrail("User retrieved by id", u.getId());
                } catch (JsonProcessingException e) {
                    logger.error("Error while creating log message: {}", e.getMessage());
                }
            });
            return user;
    }
    public String deleteUserById(Long id)//delete user by id
    {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            logAuditTrail("User deleted successfully", id);
            return "deleted successfully";
        } else {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }
    //need to implement find by name

    public User updateUserById(Long id, User user) {   //edit user by id
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User existingUser = userData.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPassword(user.getPassword());

            existingUser.setEmail(user.getEmail());

            User updatedUser = userRepository.save(existingUser);
            logAuditTrail("User updated successfully", updatedUser.getId());

            return updatedUser;
        } else {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }

    public Optional<User> findUserByEmail(String email)//find user by email
    {
        Optional<User> user = userRepository.findUserByEmail(email);
        user.ifPresent(u -> {
            try {
                String logMessage = objectMapper.writeValueAsString(u);
                logger.info("Retrieved user by email {}: {}", email, logMessage);
                logAuditTrail("User retrieved by email", u.getId());
            } catch (JsonProcessingException e) {
                logger.error("Error while creating log message: {}", e.getMessage());
            }
        });
        return userRepository.findUserByEmail(email);
    }



    public String loginUser(User users) {
        User user = userRepository.findUserByEmail(users.getEmail()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
//        if(!bCryptPasswordEncoder.matches(users.getPassword(),user.getPassword()))

        if (!user.getPassword().equals(users.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String jwt = JwtUtil.generateToken(users.getEmail());

        return jwt;
}
//    public String resetPassword(User UserExist) {
//        // Find the user by email
//        Optional<User> user = userRepository.findUserByEmail(UserExist.getEmail());
//        if (user == null) {
//            return"User not found";
//        }
//
//        // Update the user's password
//        if (user.isPresent()) {
//            User existingUser = user.get();
//            existingUser.setPassword(UserExist.getNewPassword());
//            User updatedUser = userRepository.save(existingUser);
//        }
//        // Return a success response
//        return "Password reset successfully";
//    }
private void logAuditTrail(String action, Long userId) {
    logger.info("Audit trail - action: {}, user id: {}, timestamp: {}", action, userId, new Date());
}
}


