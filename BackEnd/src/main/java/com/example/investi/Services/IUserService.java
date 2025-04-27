package com.example.investi.Services;
import com.example.investi.Entities.User;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);


    void deleteUser(Long id);

    }



