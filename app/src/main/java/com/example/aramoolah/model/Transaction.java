package com.example.aramoolah.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(
    tableName = "transaction_table",
    indices = {
        @Index(value = {"walletId"}),
        @Index(value = {"itemId"})
    },
    foreignKeys = {
        @ForeignKey(entity = Wallet.class, parentColumns = "walletId", childColumns = "walletId", onUpdate = 5, onDelete = 5),
        @ForeignKey(entity = Item.class, parentColumns = "itemId", childColumns = "itemId", onDelete = 5, onUpdate = 5)
    }
)
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public int transactionId;
    public int walletId;
    public int itemId;
    public TransactionType transactionType;

    public int amountOfMoney;
    public int numberOfItem;
    public LocalDateTime dateTime;

}
