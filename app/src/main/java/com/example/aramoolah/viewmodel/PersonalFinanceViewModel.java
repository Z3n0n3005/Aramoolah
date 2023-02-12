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
import com.example.aramoolah.model.ItemCategory;
import com.example.aramoolah.model.TransactionType;
import com.example.aramoolah.model.User;
import com.example.aramoolah.model.Wallet;
import com.example.aramoolah.repository.ItemRepository;
import com.example.aramoolah.repository.TransactionRepository;
import com.example.aramoolah.model.Transaction;
import com.example.aramoolah.repository.UserRepository;
import com.example.aramoolah.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.List;

public class PersonalFinanceViewModel extends AndroidViewModel {
    LiveData<List<Transaction>> readAllTransactionOfCurrentUser;
    LiveData<List<Item>> readAllItemOfCurrentUser;
    LiveData<List<Wallet>> readAllWalletOfCurrentUser;
    User currentUser;


    TransactionRepository transactionRepository;
    ItemRepository itemRepository;
    WalletRepository walletRepository;
    UserRepository userRepository;

    public PersonalFinanceViewModel(@NonNull Application application) throws InterruptedException {
        super(application);

        //User
        UserDao userDao = UserDatabase.getUserDatabase(application).userDao();
        userRepository = new UserRepository(userDao);
        currentUser = getUser("John@gmail.com");

        // Transaction
        TransactionDao transactionDao = TransactionDatabase.getTransactionDatabase(application).transactionDao();
        transactionRepository = new TransactionRepository(transactionDao);
        readAllTransactionOfCurrentUser = transactionDao.getAllTransaction();

        // Item
        ItemDao itemDao = ItemDatabase.getItemDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);
        readAllItemOfCurrentUser = itemRepository.getAllItem();

        //Wallet
        WalletDao walletDao = WalletDatabase.getWalletDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);
        readAllWalletOfCurrentUser = walletRepository.getAllWallet();

    }

    // Transaction
    public void addTransaction(Transaction transaction){
        new Thread(new Runnable() {
            @Override
            public void run() {transactionRepository.addTransaction(transaction);}
        }).start();
    }

    /**
     * Another way to add transaction
     * @param amountOfMoney
     * @param numberOfItem
     * @param transactionType
     * @param walletId
     * @param itemId
     * @throws InterruptedException
     */
    public void addTransaction(
            Integer amountOfMoney,
            Integer numberOfItem,
            TransactionType transactionType,
            Integer walletId,
            Integer itemId
    ) throws InterruptedException {
        Transaction transaction = new Transaction();
        transaction.walletId = walletId;
        transaction.dateTime = LocalDateTime.now();
        transaction.itemId = itemId;
        transaction.amountOfMoney = amountOfMoney;
        transaction.numberOfItem = numberOfItem;
        transaction.transactionType = transactionType;
        addTransaction(transaction);
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

    public int getItemId(String itemName) throws InterruptedException {
        class Foo implements Runnable {
            private volatile int result;
            @Override
            public void run() {
                result = itemRepository.getItemId(itemName);
            }

            public int getResult() {
                return result;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
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

    public int getWalletId(String walletName) throws InterruptedException {
        class Foo implements Runnable {
            private volatile int result;
            @Override
            public void run() {
                result = walletRepository.getWalletId(walletName);
            }

            public int getResult() {
                return result;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    // User
    public void addUser(User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.addUser(user);
            }
        }).start();
    }

    public void updateUser(User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.updateUser(user);
            }
        }).start();
    }

    public void deleteUser(User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.deleteUser(user);
            }
        }).start();
    }

    public int getUserId(String email) throws InterruptedException {
        class Foo implements Runnable {
            private volatile int result;
            @Override
            public void run() {
                result = userRepository.getUserId(email);
            }

            public int getResult() {
                return result;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
    public User getUser(String email) throws InterruptedException{
        class Foo implements Runnable {
            private volatile User result;
            @Override
            public void run() {
                result = userRepository.getUser(email);
            }

            public User getResult() {
                return result;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
}
