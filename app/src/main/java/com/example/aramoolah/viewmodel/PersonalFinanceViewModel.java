package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.ItemCategoryDao;
import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.dao.TransactionDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.data.repository.ItemCategoryRepository;
import com.example.aramoolah.data.repository.ItemRepository;
import com.example.aramoolah.data.repository.TransactionRepository;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.data.repository.WalletRepository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PersonalFinanceViewModel extends AndroidViewModel {
    public User currentUser;

    protected MutableLiveData<List<Transaction>> currentUserTransactionList;
    public MutableLiveData<List<Item>> currentUserItemList;
    public MutableLiveData<List<ItemCategory>> currentUserItemCategoryList;
    public MutableLiveData<List<Wallet>> currentUserWalletList;


    TransactionRepository transactionRepository;
    ItemRepository itemRepository;
    ItemCategoryRepository itemCategoryRepository;
    WalletRepository walletRepository;
    UserRepository userRepository;

    public PersonalFinanceViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        PersonalFinanceDatabase instance = PersonalFinanceDatabase.getPersonalFinanceDatabase(application);

        //User
        UserDao userDao = instance.userDao();
        userRepository = new UserRepository(userDao);


        // Transaction
        TransactionDao transactionDao = instance.transactionDao();
        transactionRepository = new TransactionRepository(transactionDao);

        // Item
        ItemDao itemDao = instance.itemDao();
        itemRepository = new ItemRepository(itemDao);

        // ItemCategory
        ItemCategoryDao itemCategoryDao = instance.itemCategoryDao();
        itemCategoryRepository = new ItemCategoryRepository(itemCategoryDao);

        //Wallet
        WalletDao walletDao = instance.walletDao();
        walletRepository = new WalletRepository(walletDao);
    }

    // Transaction
    public void addTransaction(Transaction transaction, BigInteger cost){
        new Thread(() -> {transactionRepository.addTransaction(transaction);}).start();
    }

    public void addTransaction(Transaction transaction){
        BigInteger temp = BigInteger.ZERO;
        addTransaction(transaction, temp);
    }


    public Transaction getTransaction(Long transactionId) throws InterruptedException {
        class Foo implements Runnable{
            private volatile Transaction transaction;

            @Override
            public void run() {
                transaction = transactionRepository.getTransaction(transactionId);
            }

            public Transaction getResult(){
                return transaction;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public MutableLiveData<List<Transaction>> getCurrentUserTransactionList() throws InterruptedException {
        class Foo implements Runnable{
            private volatile MutableLiveData<List<Transaction>> currentUserTransaction;
            @Override
            public void run() {
                if(currentUserTransactionList == null) {
                    Map<Integer, List<Long>> currentUserTransactionId = transactionRepository.getUserTransactionList();
                    List<Long> transactionIdList = currentUserTransactionId.get(currentUser.userId);
                    List<Transaction> transactionList = new ArrayList<>();
                    if(transactionIdList != null){
                        for (Long transactionId : transactionIdList) {
                            try {
                                transactionList.add(getTransaction(transactionId));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    currentUserTransaction = new MutableLiveData<>(transactionList);
                } else {
                    currentUserTransaction = currentUserTransactionList;
                }
            }
            public MutableLiveData<List<Transaction>> getResult(){
                return currentUserTransaction;
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
        new Thread(() -> itemRepository.addItem(item)).start();
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

    public Item getItem(Integer itemId) throws InterruptedException {
        class Foo implements Runnable {
            private volatile Item result;
            @Override
            public void run() {
                result = itemRepository.getItem(itemId);
            }

            public Item getResult() {
                return result;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public List<Item> getItemList(Integer itemCategoryId) throws InterruptedException {
        class Foo implements Runnable{
            final List<Item> currentItemList = getCurrentUserItemList().getValue();
            final List<Item> filteredItemList = new ArrayList<>();

            Foo() throws InterruptedException {
            }

            @Override
            public void run() {
                assert currentItemList != null;
                for(Item item: currentItemList){
                    if(Objects.equals(item.itemCategoryId, itemCategoryId)) {
                        filteredItemList.add(item);
                    }
                }
            }

            public List<Item> getResult(){
                return filteredItemList;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public MutableLiveData<List<Item>> getCurrentUserItemList() throws InterruptedException {
        class Foo implements Runnable{
            private volatile MutableLiveData<List<Item>> currentUserItem;
            @Override
            public void run() {
                if(currentUserItemList == null) {
                    Map<Integer, List<Integer>> currentUserItemId = itemRepository.getUserItemList();
                    List<Integer> itemIdList = currentUserItemId.get(currentUser.userId);
                    List<Item> itemList = new ArrayList<>();
                    if(itemIdList != null) {
                        for (Integer itemId : itemIdList) {
                            try {
                                itemList.add(getItem(itemId));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    currentUserItem = new MutableLiveData<>(itemList);
                } else {
                    currentUserItem = currentUserItemList;
                }
            }
            public MutableLiveData<List<Item>> getResult(){
                return currentUserItem;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    // Item Category
    public MutableLiveData<List<ItemCategory>> getCurrentUserItemCategoryList() throws InterruptedException {
        class Foo implements Runnable{
            MutableLiveData<List<ItemCategory>> currentUserItemCategory;
            @Override
            public void run() {
                if(currentUserItemCategoryList == null){
                    Map<Integer, List<Integer>> currentUserItemCategoryIdMap = itemCategoryRepository.getUserItemCategory();
                    List<Integer> itemCategoryIdList = currentUserItemCategoryIdMap.get(currentUser.userId);
                    List<ItemCategory> itemCategoryList = new ArrayList<>();
                    if(itemCategoryIdList != null){
                        for (Integer itemCategoryId : itemCategoryIdList) {
                            try {
                                itemCategoryList.add(getItemCategory(itemCategoryId));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    currentUserItemCategory = new MutableLiveData<>(itemCategoryList);
                } else {
                    currentUserItemCategory = currentUserItemCategoryList;
                }
            }

            public MutableLiveData<List<ItemCategory>> getResult(){
                return currentUserItemCategory;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public ItemCategory getItemCategory(Integer itemCategoryId) throws InterruptedException {
        class Foo implements Runnable{
            ItemCategory result;
            @Override
            public void run() {
                result = itemCategoryRepository.getItemCategory(itemCategoryId);
            }

            public ItemCategory getResult(){
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
        new Thread(() -> walletRepository.addWallet(wallet)).start();
    }

    public void addWallet(String walletName, Long initialAmount){}

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

    public MutableLiveData<List<Wallet>> getCurrentUserWalletList() throws InterruptedException {
        class Foo implements Runnable{
            private volatile MutableLiveData<List<Wallet>> currentUserWallet = new MutableLiveData<>();
            @Override
            public void run() {
                if(currentUserWalletList == null) {
                    Map<Integer, List<Integer>> currentUserWalletId = userRepository.getCurrentUserWalletList();
                    List<Integer> walletIdList = currentUserWalletId.get(currentUser.userId);
                    List<Wallet> walletList = new ArrayList<>();
                    if(walletIdList != null) {
                        for (Integer walletId : walletIdList) {
                            try {
                                walletList.add(getWallet(walletId));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    currentUserWallet = new MutableLiveData<>(walletList);
                } else {
                    currentUserWallet = currentUserWalletList;
                }
            }
            public MutableLiveData<List<Wallet>> getResult(){
                return currentUserWallet;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    // User
    // TODO: Remove add user
    public void addUser(User user){
        new Thread(() -> userRepository.addUser(user)).start();
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

    public User getUser(Integer userId) throws InterruptedException {
        class Foo implements Runnable {
            private volatile User result;
            @Override
            public void run() {
                result = userRepository.getUser(userId);
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

    public void setCurrentUser(Integer userId) throws InterruptedException {
        this.currentUser = getUser(userId);
    }
}