package com.example.aramoolah.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.dao.TransactionDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.data.repository.ItemRepository;
import com.example.aramoolah.data.repository.TransactionRepository;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.data.repository.WalletRepository;

import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalFinanceViewModel extends AndroidViewModel {
    LiveData<List<Transaction>> readAllTransactionOfCurrentUser;
    LiveData<List<Item>> readAllItemOfCurrentUser;
    LiveData<List<Wallet>> readAllWalletOfCurrentUser;
    public User currentUser;


    TransactionRepository transactionRepository;
    ItemRepository itemRepository;
    WalletRepository walletRepository;
    UserRepository userRepository;

    public PersonalFinanceViewModel(@NonNull Application application) throws InterruptedException {
        super(application);

        //User
        UserDao userDao = PersonalFinanceDatabase.getTransactionDatabase(application).userDao();
        userRepository = new UserRepository(userDao);
//        currentUser = getUser("John@gmail.com");

        // Transaction
        TransactionDao transactionDao = PersonalFinanceDatabase.getTransactionDatabase(application).transactionDao();
        transactionRepository = new TransactionRepository(transactionDao);
//        readAllTransactionOfCurrentUser = transactionDao.getAllTransaction();

        // Item
        ItemDao itemDao = PersonalFinanceDatabase.getTransactionDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);
//        readAllItemOfCurrentUser = itemRepository.getAllItem();

        //Wallet
        WalletDao walletDao = PersonalFinanceDatabase.getTransactionDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);
//        readAllWalletOfCurrentUser = walletRepository.getAllWallet();

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

    public Map<Integer, List<Integer>> getCurrentUserTransaction() throws InterruptedException {
        class Foo implements Runnable{
            private volatile Map<Integer, List<Integer>> readAllTransaction;
            @Override
            public void run() {
                readAllTransaction = userRepository.getAllTransactionOfCurrentUser();
            }
            public Map<Integer, List<Integer>> getResult(){
                return readAllTransaction;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public List<Wallet> getCurrentUserWallet() throws InterruptedException {
        class Foo implements Runnable{
            private volatile Map<Integer,List<Integer>> readCurrentUserWalletId;
            private volatile List<Wallet> readCurrentUserWallet = new ArrayList<>();
            @Override
            public void run() {
                readCurrentUserWalletId = userRepository.getAllWalletOfCurrentUser();
                List<Integer> walletIdList = readCurrentUserWalletId.get(currentUser.userId);
                for(Integer walletId : walletIdList){
                    try {
                        readCurrentUserWallet.add(getWallet(walletId));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            public List<Wallet> getResult(){
                return readCurrentUserWallet;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
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

    public Wallet getWallet(Integer walletId) throws InterruptedException {
        class Foo implements Runnable {
            private volatile Wallet result;
            @Override
            public void run() {
                result = walletRepository.getWallet(walletId);
            }

            public Wallet getResult() {
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

    public void setCurrentUser(String email) throws InterruptedException {
        this.currentUser = getUser(email);
    }
}
