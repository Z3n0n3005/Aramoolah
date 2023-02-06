package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.TransactionDao;
import com.example.aramoolah.data.TransactionDatabase;
import com.example.aramoolah.data.TransactionRepository;
import com.example.aramoolah.model.Transaction;

import java.util.List;

public class PersonalFinanceViewModel extends AndroidViewModel {
    LiveData<List<Transaction>> readAllTransaction;
    TransactionRepository transactionRepository;
    public PersonalFinanceViewModel(@NonNull Application application) {
        super(application);
        TransactionDao transactionDao = TransactionDatabase.getTransactionDatabase(application).transactionDao();
        transactionRepository = new TransactionRepository(transactionDao);
        readAllTransaction = transactionDao.getAllTransaction();
    }

    public void addTransaction(Transaction transaction){
        new Thread(new Runnable() {
            @Override
            public void run() {transactionRepository.addTransaction(transaction);}
        }).start();
    }

    public void updateTransaction(Transaction transaction){
        new Thread(new Runnable() {
            @Override
            public void run() {transactionRepository.addTransaction(transaction);}
        }).start();
    }

    public void deleteTransaction(Transaction transaction){
        new Thread(new Runnable() {
            @Override
            public void run() {transactionRepository.deleteTransaction(transaction);}
        }).start();
    }
}
