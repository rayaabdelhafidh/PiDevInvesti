package com.example.investiprojet.Repositories;

import com.example.investiprojet.entities.User;
import com.example.investiprojet.entities.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface  UserRepository extends JpaRepository<User,Long> {


        // Recherche par nom ou prénom (contient une partie du texte)
        Page<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

        // Recherche par email
        Optional<User> findByEmail(String email);

        // Recherche par rôle
        Page<User> findByRole(String role);

        // Recherche par statut (actif, suspendu…)
        Page<User> findByStatut(UserStatus statut);

        // Recherche avancée (nom + rôle + statut)

        @Query("SELECT u FROM User u WHERE " +
                "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
                "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
                "(:email IS NULL OR u.email LIKE %:email%) AND " +
                "(:role IS NULL OR u.role = :role) AND " +
                "(:statut IS NULL OR u.statut = :statut)")
        <pageable>
        Page<User> searchUsers(@Param("firstName") String firstName,
                               @Param("lastName") String lastName,
                               @Param("email") String email,
                               @Param("role") String role,
                               @Param("statut") UserStatus statut ,
                                Pageable pageable);
        Page<User> findAll(Pageable pageable);



}





