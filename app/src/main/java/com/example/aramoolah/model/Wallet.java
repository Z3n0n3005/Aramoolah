package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallet_table")
public class Wallet {
    @PrimaryKey(autoGenerate = true)
    int walletId;
    String firstName;
    String middleName;
    String lastName;

}
