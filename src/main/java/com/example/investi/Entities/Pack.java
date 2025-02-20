package com.example.investi.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Data
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PackId ;
    private String Nom;
    private String Intervalle;
    private String Description ;
    private String Categorie ;

}
