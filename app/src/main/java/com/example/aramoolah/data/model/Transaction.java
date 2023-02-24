package com.example.aramoolah.data.model;

import androidx.annotation.LongDef;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

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
    @ColumnInfo(name = "transactionId")
    public long transactionId;
    @ColumnInfo(name = "walletId")
    public Integer walletId;
    @ColumnInfo(name = "itemId")
    public Integer itemId;
    @ColumnInfo(name = "transactionType")
    public TransactionType transactionType;
    @ColumnInfo(name = "amountOfMoney")
    public Money amountOfMoney;
    @ColumnInfo(name = "numberOfItem")
    public Integer numberOfItem;
    @ColumnInfo(name = "localDateTime")
    public LocalDateTime localDateTime;

    public Transaction(Integer walletId, Integer itemId, TransactionType transactionType, Money amountOfMoney, Integer numberOfItem, LocalDateTime localDateTime){
        this.walletId = walletId;
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.amountOfMoney = amountOfMoney;
        this.numberOfItem = numberOfItem;
        this.localDateTime = localDateTime;
    }

}
