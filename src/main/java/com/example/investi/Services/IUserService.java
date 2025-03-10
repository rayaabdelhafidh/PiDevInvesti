package com.example.investi.Services;
import com.example.investi.Entities.User;

import java.util.List;


public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    User getUserByEmail(String email);
    void deleteUser(Long id);

    }



