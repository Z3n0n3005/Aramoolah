package com.example.aramoolah.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Update
    void updateTransaction(Transaction transaction);

    @Insert
    void addTransaction(Transaction transaction);

    @Delete
    void deleteTransaction(Transaction transaction);

    @Query("SELECT * FROM transaction_table ORDER BY transactionId")
    LiveData<List<Transaction>> getAllTransaction();

}
