package com.example.aramoolah.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    int transactionId;
    //TODO: add foreign key
    int walletId;
    //TODO: learn typeconverter
    @Embedded
    TransactionType transactionType;
    //TODO: learn foreignkey
    int itemId;
    LocalDateTime dateTime;

    public Transaction(){}

}
