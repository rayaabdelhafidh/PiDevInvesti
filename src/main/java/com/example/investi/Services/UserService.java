package com.example.investi.Services;

import com.example.investi.Entities.User;
import com.example.investi.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


    @Service
    @AllArgsConstructor
    public class UserService implements IUserService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public User createUser(User user) {
            return userRepository.save(user);
        }

        @Override
        public User getUserById(Long id) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        public User getUserByEmail(String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        @Override
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

        @Override
        public User updateUser(Long id, User userDetails) {
            return userRepository.findById(id).map(existingUser -> {
                existingUser.setFirstName(userDetails.getFirstName());
                existingUser.setLastName(userDetails.getLastName());
                existingUser.setPassword(userDetails.getPassword());
                existingUser.setEmail(userDetails.getEmail());
                existingUser.setAdresse(userDetails.getAdresse());
                existingUser.setPhonenumber(userDetails.getPhonenumber());
                return userRepository.save(existingUser);
            }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        }


        @Override
        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
        
    }



