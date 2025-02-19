package com.example.investi.Repositories;

import com.example.investi.Entities.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<user,Long> {
}
