package com.example.aramoolah.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.data.model.Transaction;

import java.util.List;
import java.util.Map;

@Dao
public interface TransactionDao {
    @Update
    void updateTransaction(Transaction transaction);

    @Insert
    void addTransaction(Transaction transaction);

    @Delete
    void deleteTransaction(Transaction transaction);

    @Query("SELECT * FROM transaction_table WHERE transactionId = :transactionId")
    Transaction getTransaction(Long transactionId);

    @Query("SELECT * FROM transaction_table ORDER BY transactionId")
    LiveData<List<Transaction>> getAllTransaction();

    @MapInfo(keyColumn = "userId", valueColumn = "transactionId")
    @Query("SELECT * FROM transaction_table t JOIN wallet_table w ON t.walletId = w.walletId JOIN user_table u ON u.userId = w.userId")
    Map<Integer, List<Long>> getUserTransactionList();
}
