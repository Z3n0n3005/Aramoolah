package com.example.aramoolah.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.data.model.Wallet;

import org.javamoney.moneta.Money;

import java.util.List;
import java.util.Map;

@Dao
public interface WalletDao {
    @Insert
    void addWallet(Wallet wallet);

    @Update
    void updateWallet(Wallet wallet);

    @Query("UPDATE wallet_table SET totalAmount = :totalAmount WHERE walletId = :walletId")
    void updateTotalAmount(Integer walletId, Money totalAmount);

    @Delete
    void deleteWallet(Wallet wallet);

    @Query("SELECT * FROM wallet_table ORDER BY walletId")
    LiveData<List<Wallet>> getAllWallet();

    @Query("SELECT walletId FROM wallet_table WHERE walletName = :walletName")
    int getWalletId(String walletName);

    @Query("SELECT * FROM wallet_table WHERE walletId = :walletId")
    Wallet getWallet(Integer walletId);

    @MapInfo(keyColumn = "walletId", valueColumn = "transactionId")
    @Query("SELECT * FROM transaction_table t JOIN wallet_table w ON t.walletId = w.walletId")
    Map<Integer, List<Integer>> getWalletTransactionList();
}
