package com.example.aramoolah.data.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.TransactionDao;
import com.example.aramoolah.data.model.Transaction;

import java.util.List;

public class TransactionRepository {
    private TransactionDao transactionDao;
    public TransactionRepository(TransactionDao transactionDao){
        this.transactionDao = transactionDao;
    }
    public void updateTransaction(Transaction transaction){
        transactionDao.updateTransaction(transaction);
    }
    public void deleteTransaction(Transaction transaction){
        transactionDao.deleteTransaction(transaction);
    }
    public void addTransaction(Transaction transaction){
        transactionDao.addTransaction(transaction);
    }
    public LiveData<List<Transaction>> getAllTransaction(){
        return transactionDao.getAllTransaction();
    }
}
