package com.example.aramoolah.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;


@Entity(tableName = "wallet_table",
    indices = {
        @Index(value = {"walletName"}, unique = true),
        @Index(value = {"userId"})
    },
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = {"userId"}, childColumns = {"userId"}, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    }
)
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    public Integer walletId;
    public Integer userId;
    public String walletName;
    public Money totalAmount;

    public Wallet(Integer userId, String walletName, Money totalAmount){
        this.userId = userId;
        this.walletName = walletName;
        this.totalAmount = totalAmount;
    }
}
