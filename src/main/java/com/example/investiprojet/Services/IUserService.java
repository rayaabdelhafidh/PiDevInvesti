package com.example.investiprojet.Services;
import com.example.investiprojet.entities.User;

import java.util.List;
import java.util.Optional;
import com.example.investiprojet.entities.User;


import com.example.investiprojet.entities.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;



public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();

    Page<User> getAllUsers(Pageable pageable);

    User updateUser(Long id, User user);


    void deleteUser(Long id);
    Page<User> searchUsers(String firstName, String lastName, String email, String role, UserStatus statut, Pageable pageable);





}









