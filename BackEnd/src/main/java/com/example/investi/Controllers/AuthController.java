package com.example.investi.Controllers;

import com.example.investi.Entities.LoginRequest;
import com.example.investi.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        if (token != null) {
            Map<String, String> response = new HashMap<>();
            response.put("token", token); // Wrap the token in a JSON object
            return ResponseEntity.ok(response); // Return the JSON response
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid credentials"));
        }
    }
}
