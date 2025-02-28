package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.User;
import com.example.pidevinvesti.Repositories.UserRepository;
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

        @Override
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).get();
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhonenumber(userDetails.getPhonenumber());
        user.setCreationdate(userDetails.getCreationdate());
        user.setEmail(userDetails.getEmail());
        user.setAdresse(userDetails.getAdresse());
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

        @Override
        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
    }



