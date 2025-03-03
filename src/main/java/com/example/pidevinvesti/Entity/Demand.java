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
    @JsonProperty("Status")
    @Enumerated(EnumType.STRING)
    private DemandStatus Status;

   @OneToOne(mappedBy = "demand")
   private Loan loan ;

    @ManyToOne
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @OneToMany(mappedBy = "demand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collateral> collaterals;

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public List<Collateral> getCollaterals() {
        return collaterals;
    }

    public void setCollaterals(List<Collateral> collaterals) {
        this.collaterals = collaterals;
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
        return Status;
    }

    public void setStatus(DemandStatus status) {
        Status = status;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
