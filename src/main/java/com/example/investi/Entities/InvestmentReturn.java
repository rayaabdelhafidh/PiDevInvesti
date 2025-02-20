package com.example.investi.Entities;

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

    @ManyToOne
    @JsonIgnore
    private Investment investment; // ROI is calculated per investment

    private BigDecimal roiPercentage; // e.g., 20% means 0.20
    private BigDecimal totalReturn; // Amount this investor gets

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date payoutDate; // When the ROI is paid

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

    public void setPayoutDate(Date payoutDate) {
        this.payoutDate = payoutDate;
    }
}

