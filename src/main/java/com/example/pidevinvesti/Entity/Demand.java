package com.example.pidevinvesti.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Demand {
    @Id
    private long DemandId;
    private float Amount ;
    private float IntrestRate ;
    private int Duration ;
    private Date DemandDate ;
    private DemandStatus Status;
   // @OneToOne
   // private Loan loan ;

}
