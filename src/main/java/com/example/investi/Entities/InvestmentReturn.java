package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class InvestmentReturn {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int returnId;

    private BigDecimal roiPercentage;
    private BigDecimal totalReturn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date payoutDate; // When the ROI is paid

    //Relations
    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    private Investment investment;
    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    private Investor investor;

    // Default constructor
    public InvestmentReturn() {}

    // Parameterized constructor
    public InvestmentReturn(Investment investment, BigDecimal roiPercentage, BigDecimal totalReturn, Date payoutDate) {
        this.investment = investment;
        this.roiPercentage = roiPercentage;
        this.totalReturn = totalReturn;
        this.payoutDate = payoutDate;
    }

    // Getters and Setters
    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public Investment getInvestment() {
        return investment;
    }

    public void setInvestment(Investment investment) {
        this.investment = investment;
    }

    public BigDecimal getRoiPercentage() {
        return roiPercentage;
    }

    public void setRoiPercentage(BigDecimal roiPercentage) {
        this.roiPercentage = roiPercentage;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public Date getPayoutDate() {
        return payoutDate;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public void setPayoutDate(Date payoutDate) {
        this.payoutDate = payoutDate;
    }
}


