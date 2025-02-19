package com.example.investiprojet.Controllers;

import com.example.investiprojet.Services.IUserService;
import com.example.investiprojet.entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
@RequestMapping("/User")

public class UserControllers {
@Autowired
    private IUserService UserService ;

    @PostMapping("/add")
    public User addUser(@RequestBody User User){
        return UserService.createUser(User);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public User getUserById(@PathVariable Long id) {
        return UserService.getUserById(id);

    }
    @GetMapping("/all")
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return UserService.getAllUsers();
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a user")
    public void deleteUser(@PathVariable Long id) {
        UserService.deleteUser(id);
    }
    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return UserService.updateUser(id, user);
    }

}


