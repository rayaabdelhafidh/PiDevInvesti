package com.example.investi.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class StockInvestment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String stockSymbol;  // Symbole de l'action (ex: AAPL, TSLA)
    private BigDecimal amountInvested; // Montant investi
    private int quantity; // Nombre d’actions achetées
    private BigDecimal purchasePrice; // Prix d’achat par action

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    @ManyToOne
    private Investor investor;  // L'investisseur qui a acheté

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public BigDecimal getAmountInvested() {
        return amountInvested;
    }

    public void setAmountInvested(BigDecimal amountInvested) {
        this.amountInvested = amountInvested;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }
}

