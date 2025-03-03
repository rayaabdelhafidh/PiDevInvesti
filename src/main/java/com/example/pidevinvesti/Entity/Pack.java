package com.example.pidevinvesti.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    @JsonProperty("Nom")
    private String Nom;
    @JsonProperty("Intervalle")
    private String Intervalle;
    @JsonProperty("Description")
    private String Description ;
    @JsonProperty("Categorie")
    private String Categorie ;

    @OneToMany(mappedBy = "pack")
    private List<Demand> demands;
    public long getPackId() {
        return PackId;
    }

    public void setPackId(long packId) {
        PackId = packId;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getIntervalle() {
        return Intervalle;
    }

    public void setIntervalle(String intervalle) {
        Intervalle = intervalle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

}
