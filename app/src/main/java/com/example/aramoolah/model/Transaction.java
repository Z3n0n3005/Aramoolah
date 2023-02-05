package com.example.aramoolah.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction")
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


}
