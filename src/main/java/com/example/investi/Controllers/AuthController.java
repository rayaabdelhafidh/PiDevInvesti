package com.example.investi.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.investi.Services.AuthService;
import com.example.investi.Utils.JwtUtil;
import com.example.investi.Services.IUserService;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private IUserService UserService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String token = authService.login(credentials.get("username"), credentials.get("password"));
        return Map.of("token", token);
    }

     @GetMapping("/activate/{email}/{token}")
    public String activate(@PathVariable String token, @PathVariable String email)
    {
        boolean isTokenValid = jwtUtil.validateToken(token,email);

        if(isTokenValid)
        {
            var existingUser = UserService.getUserByEmail(email);
            existingUser.setEnabled(true);
            UserService.updateUser(existingUser.getId(), existingUser);
            return "Your account has been activated successfully. Go to login page";
        }
        return "Your token expired";
    }
}
