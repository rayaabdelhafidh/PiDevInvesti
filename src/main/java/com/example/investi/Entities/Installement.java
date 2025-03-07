package com.example.investi.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Installement {
    @Id
    private long InstId ;
    private float Amount;
    private Date DateEcheance ;
    private String Status ;

}
