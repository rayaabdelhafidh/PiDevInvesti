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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String name;
    private Float revenuMensuel;
    private Integer scoreCredit;
    private Date dateInscription;
    private Date birthDate;
    private Integer totalLateLoans = 0;
    private Integer totalRefusedDemands = 0;
    private String email;


    @OneToMany(mappedBy = "user")
    private List<Demand> demands;

    @OneToMany(mappedBy = "user")
    private List<Collateral> collaterals;

    public List<Collateral> getCollaterals() {
        return collaterals;
    }

    public void setCollaterals(List<Collateral> collaterals) {
        this.collaterals = collaterals;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRevenuMensuel() {
        return revenuMensuel;
    }

    public void setRevenuMensuel(Float revenuMensuel) {
        this.revenuMensuel = revenuMensuel;
    }

    public Integer getScoreCredit() {
        return scoreCredit;
    }

    public void setScoreCredit(Integer scoreCredit) {
        this.scoreCredit = scoreCredit;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getTotalLateLoans() {
        return totalLateLoans;
    }

    public void setTotalLateLoans(Integer totalLateLoans) {
        this.totalLateLoans = totalLateLoans;
    }

    public Integer getTotalRefusedDemands() {
        return totalRefusedDemands;
    }

    public void setTotalRefusedDemands(Integer totalRefusedDemands) {
        this.totalRefusedDemands = totalRefusedDemands;
    }

    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
