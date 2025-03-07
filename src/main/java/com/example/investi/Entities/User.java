package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
public class User implements UserDetails { // Implémente UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("creationdate")
    private LocalDateTime creationdate;

    @JsonProperty("email")
    private String email;

    @JsonProperty("adresse")
    private String adresse;

    @JsonProperty("phonenumber")
    private String phonenumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Récupère le type d'utilisateur à partir de la table 'user_type'
        String userType = this.getClass().getSimpleName().toUpperCase(); // e.g., "CLIENT", "TRAINER"
        String role = "ROLE_" + userType; // Format Spring Security (ROLE_*)
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return this.email; // Utilisez l'email comme nom d'utilisateur
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Par défaut, le compte n'expire pas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Par défaut, le compte n'est pas bloqué
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Par défaut, les credentials n'expirent pas
    }

    @Override
    public boolean isEnabled() {
        return true; // Par défaut, le compte est activé
    }
}