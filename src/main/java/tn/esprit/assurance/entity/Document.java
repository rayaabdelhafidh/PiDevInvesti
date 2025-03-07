package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("document_id")
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "sinister_id", nullable = false)
    @JsonIgnoreProperties("documents")
    @JsonProperty("sinister")
    @JsonIgnore

    private Sinister sinister;

    @JsonProperty("original_filename")
    private String originalFilename; // Nom original du fichier

    @JsonProperty("document_path")
    private String documentPath; // Chemin du fichier stocké

    @JsonProperty("file_type")
    private String fileType; // Type de fichier (ex: application/pdf, image/png)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("upload_date")
    private LocalDate uploadDate;

    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDate.now();
    }
}
