package com.example.aramoolah.data;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.model.Transaction;

import java.util.List;

public class TransactionRepository {
    private TransactionDao transactionDao;
    public TransactionRepository(TransactionDao transactionDao){
        this.transactionDao = transactionDao;
    }
    public void updateTransaction(Transaction transaction){
        updateTransaction(transaction);
    }
    public void deleteTransaction(Transaction transaction){
        deleteTransaction(transaction);
    }
    public void addTransaction(Transaction transaction){
        addTransaction(transaction);
    }
    public LiveData<List<Transaction>> getAllTransaction(Transaction transaction){
        return getAllTransaction(transaction);
    }
}
