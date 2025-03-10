package com.example.investi.Controllers;

import com.example.investi.Services.IUserService;
import com.example.investi.Services.IEmailService;
import com.example.investi.Utils.JwtUtil;
import com.example.investi.Entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@FieldDefaults(level= AccessLevel.PRIVATE)
@RequestMapping("/User")

public class UserControllers {
@Autowired
    private IUserService UserService;
    private IEmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public void addUser(@RequestBody User User){
        User.setEnabled(false); // L'utilisateur doit activer son compte

        User createdUser = UserService.createUser(User);

        String token = jwtUtil.generateToken(createdUser.getEmail());

        String activationLink =  "<a href=\"http://localhost:8088/api/auth/activate" + "/"   + createdUser.getEmail() + "/" + token +"\">Activate</a> " ;

        try {
            emailService.sendEmail(
                createdUser.getEmail(),
                "Activate your InvestiProjet account",
                "Please click the following link to activate your account: " + activationLink
            );
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email d'activation à {}", createdUser.getEmail(), e);
        }

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
