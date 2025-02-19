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
public class Collateral {
    @Id
    private long CollateralId ;
    private String Type ;
    private String Value ;

}
