package com.example.aramoolah.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
    @ColumnInfo(name = "walletId")
    public Integer walletId;
    @ColumnInfo(name = "userId")
    public Integer userId;
    @ColumnInfo(name = "walletName")
    public String walletName;
    @ColumnInfo(name = "totalAmount")
    public Money totalAmount;

    public Wallet(Integer userId, String walletName, Money totalAmount){
        this.userId = userId;
        this.walletName = walletName;
        this.totalAmount = totalAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return walletName;
    }
}
