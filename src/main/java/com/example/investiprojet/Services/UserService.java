package com.example.investiprojet.Services;

import com.example.investiprojet.Repositories.UserRepository;
import com.example.investiprojet.entities.User;
import com.example.investiprojet.entities.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return userRepository.findAll(); // Correction : ici on renvoie une liste complète
    }

    @Override
        public Page<User> getAllUsers(Pageable pageable) {
            return userRepository.findAll(pageable);
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



    @Override
    public Page<User> searchUsers(String firstName, String lastName, String email, String role, UserStatus statut, Pageable pageable) {
        try {
            return userRepository.searchUsers(firstName, lastName, email, role, statut, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des utilisateurs : " + e.getMessage());
        }
    }


}









