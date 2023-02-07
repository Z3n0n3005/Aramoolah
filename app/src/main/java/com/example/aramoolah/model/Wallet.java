package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

import java.util.Currency;


@Entity(tableName = "wallet_table")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    int walletId;
    int userId;
    String firstName;
    String middleName;
    String lastName;
    Money totalAmount;
}
