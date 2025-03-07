package com.example.investi.Entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Collateral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CollateralId;
    @Enumerated(EnumType.STRING)
    @JsonProperty("Type")
    private CollateralType Type;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("Value")
    private Double Value;
    @JsonProperty("DocFilePath")
    private String DocFilePath; // Chemin du fichier téléchargé

    //  @ManyToOne
    // @JoinColumn(name = "DemandId")
    // private Demand demand;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;


    //  @ManyToOne
    //@JoinColumn(name = "garant_id", nullable = true)
    //private User garant; // Garant (facultatif)


    public Long getCollateralId() {
        return CollateralId;
    }

    public void setCollateralId(Long collateralId) {
        CollateralId = collateralId;
    }

    public CollateralType getType() {
        return Type;
    }

    public void setType(CollateralType type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        Value = value;
    }

    public String getDocFilePath() {
        return DocFilePath;
    }

    public void setDocFilePath(String docFilePath) {
        DocFilePath = docFilePath;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
