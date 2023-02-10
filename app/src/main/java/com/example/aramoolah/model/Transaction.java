package com.example.aramoolah.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int transactionId;
    //TODO: add foreign key
    public int walletId;
    //TODO: learn typeconverter
    public TransactionType transactionType;
    //TODO: learn foreignkey
    public int itemId;
    public LocalDateTime dateTime;

}
