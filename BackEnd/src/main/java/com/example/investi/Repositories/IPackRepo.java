package com.example.investi.Repositories;

import com.example.investi.Entities.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPackRepo extends JpaRepository<Pack, Long> {

}