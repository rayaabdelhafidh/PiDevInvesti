package com.example.investi.Repositories;
import com.example.investi.Entities.User;

import java.util.List;
import java.util.Optional;
import com.example.investi.Entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);


    void deleteUser(Long id);

    }



