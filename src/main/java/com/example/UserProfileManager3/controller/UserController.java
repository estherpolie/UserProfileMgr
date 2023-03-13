package com.example.UserProfileManager3.controller;


import com.example.UserProfileManager3.entity.User;
import com.example.UserProfileManager3.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController


public class UserController {


    @Autowired
    private UserService userService;


    public UserController(UserService userService)
    {
        this.userService=userService;
    }


    @PostMapping("/api/users/signup/")
    public ResponseEntity<Object> createUser( @RequestBody User user) throws JsonProcessingException {

            if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("User email exist  ", HttpStatus.NOT_FOUND);
        }
        else {
            userService.save(user);

                return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody User users) {
        User user = userService.findUserByEmail(users.getEmail()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }


        if (!users.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userService.loginUser(users), HttpStatus.OK);
    }


    @GetMapping("/api/users/signup/all")
    public List<User> getAllUser( User user) {

        return userService.getAllUsers();
    }

    @GetMapping("api/users/email/{email}")
    public Optional<User> findUserByEmail(String email)
    {

        return userService.findUserByEmail(email);
    }


    @GetMapping("api/users/{id}")//FIND user by id
    public Optional<User> userId (@PathVariable Long id)
    {

        return userService.findById(id);
    }

    @DeleteMapping("api/{id}")//delete by userId
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteUserById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            // User exists
        } else {
            // User does not exist
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        }



    }

    @PutMapping("api/1/{id}")//update user by id
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) throws JsonProcessingException {
        Optional<User> userData = userService.findById(id);

        if (userData.isPresent()) {
            User existingUser = userData.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            User updatedUser = userService.save(existingUser);
            return new ResponseEntity<>(userService.updateUserById(id,updatedUser), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //if user id doesnt exist (404 not found)
        }
    }

}



