package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

import java.util.Currency;


@Entity(tableName = "wallet_table",
    indices = {
        @Index(value = {"walletName"}, unique = true),
        @Index(value = {"userId"})
    },
    foreignKeys = {
        @ForeignKey(entity = User.class, childColumns = "userId", parentColumns = "userId", onDelete = 5, onUpdate = 5)
    }
)
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    public int walletId;
    public int userId;
    public String walletName;
    public Money totalAmount;
}
