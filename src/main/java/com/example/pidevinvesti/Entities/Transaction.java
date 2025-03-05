package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


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

    public Transaction(Long transactionId, BigDecimal amount, TransactionType type, Date transactionDate, String description, TransactionStatus status,Account account, Account targetAccount, String reference, BigDecimal fee, BigDecimal balanceAfterTransaction, Date createdAt, Date updatedAt) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.description = description;
        this.status = status;
        this.account = account;
        this.targetAccount = targetAccount;
        this.reference = reference;
        this.fee = fee;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Transaction() {
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

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public BigDecimal getAmount() {
        return amount;
    }

}