package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.ItemDao;
import com.example.aramoolah.data.ItemDatabase;
import com.example.aramoolah.data.TransactionDao;
import com.example.aramoolah.data.TransactionDatabase;
import com.example.aramoolah.data.UserDao;
import com.example.aramoolah.data.UserDatabase;
import com.example.aramoolah.data.WalletDao;
import com.example.aramoolah.data.WalletDatabase;
import com.example.aramoolah.model.Item;
import com.example.aramoolah.model.User;
import com.example.aramoolah.model.Wallet;
import com.example.aramoolah.repository.ItemRepository;
import com.example.aramoolah.repository.TransactionRepository;
import com.example.aramoolah.model.Transaction;
import com.example.aramoolah.repository.UserRepository;
import com.example.aramoolah.repository.WalletRepository;

import java.util.List;

public class PersonalFinanceViewModel extends AndroidViewModel {
    LiveData<List<Transaction>> readAllTransaction;
    LiveData<List<Item>> readAllItem;
    LiveData<List<Wallet>> readAllWallet;

    TransactionRepository transactionRepository;
    ItemRepository itemRepository;
    WalletRepository walletRepository;
    UserRepository userRepository;
    public PersonalFinanceViewModel(@NonNull Application application) {
        super(application);

        // Transaction
        TransactionDao transactionDao = TransactionDatabase.getTransactionDatabase(application).transactionDao();
        transactionRepository = new TransactionRepository(transactionDao);
        readAllTransaction = transactionDao.getAllTransaction();

        // Item
        ItemDao itemDao = ItemDatabase.getItemDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);
        readAllItem = itemRepository.getAllItem();

        //Wallet
        WalletDao walletDao = WalletDatabase.getWalletDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);
        readAllWallet = walletRepository.getAllWallet();

        //User
        UserDao userDao = UserDatabase.getUserDatabase(application).userDao();
        userRepository = new UserRepository(userDao);

    }

    // Transaction
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

    // Item
    public void addItem(Item item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemRepository.addItem(item);
            }
        }).start();
    }

    public void updateItem(Item item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemRepository.updateItem(item);
            }
        }).start();
    }

    public void deleteItem(Item item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemRepository.deleteItem(item);
            }
        }).start();
    }

    // Wallet
    public void addWallet(Wallet wallet){
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletRepository.addWallet(wallet);
            }
        }).start();
    }

    public void updateWallet(Wallet wallet){
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletRepository.updateWallet(wallet);
            }
        }).start();
    }

    public void deleteWallet(Wallet wallet){
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletRepository.deleteWallet(wallet);
            }
        }).start();
    }
}
