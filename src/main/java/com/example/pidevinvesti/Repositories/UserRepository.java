package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User,Long> {

}
