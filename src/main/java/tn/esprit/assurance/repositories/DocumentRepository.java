package tn.esprit.assurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.assurance.entity.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    List<Document> findBySinisterSinisterId(Long sinisterId);

    void deleteBySinisterSinisterId(Long id);
}
