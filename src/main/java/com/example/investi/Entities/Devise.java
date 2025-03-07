package com.example.investi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Devise")
public class Devise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDevise;
    private String nomDevise;
    private String symboleDevise;
    private Double tauxChange;

    public Devise() {

    }

    public Integer getIdDevise() {
        return idDevise;
    }

    public void setIdDevise(Integer idDevise) {
        this.idDevise = idDevise;
    }

    public String getNomDevise() {
        return nomDevise;
    }

    public void setNomDevise(String nomDevise) {
        this.nomDevise = nomDevise;
    }

    public String getSymboleDevise() {
        return symboleDevise;
    }

    public void setSymboleDevise(String symboleDevise) {
        this.symboleDevise = symboleDevise;
    }

    public Double getTauxChange() {
        return tauxChange;
    }

    public void setTauxChange(Double tauxChange) {
        this.tauxChange = tauxChange;
    }
}
