package com.example.investi.Entities;

import com.example.investi.Entities.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
@Entity
public class Assureur extends User {

    // @OneToMany(mappedBy = "approvedBy")
   // private List<Sinister> approvedSinisters; //
}
