package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
