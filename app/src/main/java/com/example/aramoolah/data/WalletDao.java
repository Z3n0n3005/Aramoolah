package com.example.aramoolah.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.model.Wallet;

import java.util.List;

@Dao
public interface WalletDao {
    @Insert
    void addWallet(Wallet wallet);

    @Update
    void updateWallet(Wallet wallet);

    @Delete
    void deleteWallet(Wallet wallet);

    @Query("SELECT * FROM wallet_table ORDER BY walletId")
    LiveData<List<Wallet>> getAllWallet();

    @Query("SELECT walletId FROM wallet_table WHERE walletName = :walletName")
    int getWalletId(String walletName);
}
