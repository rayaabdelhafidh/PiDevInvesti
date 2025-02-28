package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Bonne stratégie pour une table par entité enfant
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; // ID unique pour toutes les entités héritant de User

        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("lastName")
        private String lastName;
        @JsonProperty("password")
        private String password;
        @JsonProperty("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime creationdate;
        @JsonProperty("email")
        private String email;
        @JsonProperty("adresse")
        private String adresse;
        @JsonProperty("phonenumber")
        private String phonenumber;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public LocalDateTime getCreationdate() {
                return creationdate;
        }

        public void setCreationdate(LocalDateTime creationdate) {
                this.creationdate = creationdate;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getAdresse() {
                return adresse;
        }

        public void setAdresse(String adresse) {
                this.adresse = adresse;
        }

        public String getPhonenumber() {
                return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
                this.phonenumber = phonenumber;
        }
}
