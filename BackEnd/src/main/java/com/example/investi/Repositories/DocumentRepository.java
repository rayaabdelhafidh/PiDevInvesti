package com.example.investi.Repositories;

import com.example.investi.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    List<Document> findBySinisterSinisterId(Long sinisterId);

    void deleteBySinisterSinisterId(Long id);
}
