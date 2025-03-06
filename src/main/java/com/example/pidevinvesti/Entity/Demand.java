package com.example.pidevinvesti.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Data
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long DemandId;
    @JsonProperty("Amount")
    private float Amount ;
    @JsonProperty("Duration")
    private int Duration ;
    @JsonProperty("DemandDate")
    private LocalDate DemandDate ;
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private DemandStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(mappedBy = "demand")
   private Loan loan ;

    @ManyToOne
    @JoinColumn(name = "pack_id")
    private Pack pack;


    public Pack getPack() {
        return pack;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public long getDemandId() {
        return DemandId;
    }

    public void setDemandId(long demandId) {
        DemandId = demandId;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public LocalDate getDemandDate() {
        return DemandDate;
    }

    public void setDemandDate(LocalDate demandDate) {
        DemandDate = demandDate;
    }

    public DemandStatus getStatus() {
        return status;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
