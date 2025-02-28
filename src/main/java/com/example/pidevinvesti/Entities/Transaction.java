package com.example.pidevinvesti.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne
    private Investment investment;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = true)
    private Account targetAccount;

    private String reference;

    private BigDecimal fee;

    private BigDecimal balanceAfterTransaction;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.transactionDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    public void setSenderAccount(Account senderAccount) {
        this.account = senderAccount;
    }

    public Account getSenderAccount() {
        return account;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.type = transactionType;
    }

    public TransactionType getTransactionType() {
        return type;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.targetAccount=receiverAccount;
    }
    public void setDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getDate() {
        return transactionDate;
    }

    public Investment getInvestment() {
        return investment;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setInvestment(Investment investment) {
        this.investment = investment;
    }

    public void setSender(Account sender) {
        this.account = sender;
    }

    public void setReceiver(Account receiver) {
        this.targetAccount = receiver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}