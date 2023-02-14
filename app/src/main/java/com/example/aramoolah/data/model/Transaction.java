package com.example.aramoolah.data.model;

import androidx.annotation.LongDef;
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
        @ForeignKey(entity = Wallet.class, parentColumns = "walletId", childColumns = "walletId", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Item.class, parentColumns = "itemId", childColumns = "itemId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    }
)
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    public long transactionId;
    public Integer walletId;
    public Integer itemId;
    public TransactionType transactionType;

    public Integer amountOfMoney;
    public Integer numberOfItem;
    public LocalDateTime localDateTime;

    public Transaction(Integer walletId, Integer itemId, TransactionType transactionType, Integer amountOfMoney, Integer numberOfItem, LocalDateTime localDateTime){
        this.walletId = walletId;
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.amountOfMoney = amountOfMoney;
        this.numberOfItem = numberOfItem;
        this.localDateTime = localDateTime;
    }

}
