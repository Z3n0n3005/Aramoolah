package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

import java.util.Currency;


@Entity(tableName = "wallet_table")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    public int walletId;
    public int userId;
    public String firstName;
    public String middleName;
    public String lastName;
    public Money totalAmount;
}
