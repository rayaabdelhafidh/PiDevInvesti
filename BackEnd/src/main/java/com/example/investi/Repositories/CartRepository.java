package com.example.investi.Repositories;

import com.example.investi.Entities.CarteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CarteBancaire,Long> {
}
