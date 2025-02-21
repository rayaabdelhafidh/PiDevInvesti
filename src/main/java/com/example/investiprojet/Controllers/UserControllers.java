package com.example.investiprojet.Controllers;

import com.example.investiprojet.Services.IUserService;
import com.example.investiprojet.entities.User;
import com.example.investiprojet.entities.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


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
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return UserService.getAllUsers(pageable);
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
    @GetMapping("/search")
    @Operation(summary = "Search users dynamically with pagination and sorting")
    public Page<User> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) UserStatus statut,
            @RequestParam(defaultValue = "0") int page,  // Page par défaut = 0
            @RequestParam(defaultValue = "10") int size, // Nombre d'éléments par page
            @RequestParam(defaultValue = "firstName") String sortBy, // Tri par défaut sur firstName
            @RequestParam(defaultValue = "asc") String sortDirection // Ordre croissant par défaut
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return UserService.searchUsers(firstName, lastName, email, role, statut, pageable);

    }


}












