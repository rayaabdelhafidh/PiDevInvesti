package com.example.investi.Entities;

import com.example.investi.Entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @JsonProperty("dateOfBirth")
    @NotNull(message = "Date of birth cannot be null")
    private LocalDateTime dateOfBirth;

    @JsonProperty("gender")
    @NotNull(message = "Gender cannot be null")
    private String gender;

    @JsonProperty("profession")
    @NotNull(message = "Profession cannot be null")
    private String profession;

    @JsonProperty("preferredLanguage")
    private String preferredLanguage;

    @JsonProperty("nationalId")
    @NotNull(message = "National ID cannot be null")
    private String nationalId;

    @JsonProperty("revenuMensuel")
    private Float revenuMensuel;

    @JsonProperty("scoreCredit")
    private Integer scoreCredit;

    @JsonProperty("dateInscription")
    private Date dateInscription;

    @JsonProperty("totalLateLoans")
    private Integer totalLateLoans = 0; // Use Integer instead of int

    @JsonProperty("totalRefusedDemands")
    private Integer totalRefusedDemands = 0; // Use Integer instead of int
}