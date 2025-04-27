package com.example.investi.Repositories;

import com.example.investi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User,Long> {

    Optional<Object> findByEmail(String email);
}
