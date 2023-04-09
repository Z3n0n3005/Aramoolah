package com.example.aramoolah.data.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.TransactionDao;
import com.example.aramoolah.data.model.Transaction;

import java.util.List;
import java.util.Map;

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
    public Transaction getTransaction(Long transactionId){
        return transactionDao.getTransaction(transactionId);
    }
    public LiveData<List<Transaction>> getAllTransaction(){
        return transactionDao.getAllTransaction();
    }
    public Map<Integer, List<Long>> getUserTransactionList(){
        return transactionDao.getUserTransactionList();
    }
}
