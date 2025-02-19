package com.example.investiprojet.Services;
import com.example.investiprojet.entities.User;

import java.util.List;
import java.util.Optional;
import com.example.investiprojet.entities.User;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);


    void deleteUser(Long id);

    }



