package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.ItemCategoryDao;
import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.data.repository.ItemCategoryRepository;
import com.example.aramoolah.data.repository.ItemRepository;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.data.repository.WalletRepository;
import com.example.aramoolah.util.security.Hash;

import org.javamoney.moneta.Money;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddUserViewModel extends AndroidViewModel {
    UserRepository userRepository;
    WalletRepository walletRepository;
    ItemRepository itemRepository;
    ItemCategoryRepository itemCategoryRepository;

    MutableLiveData<List<ItemCategory>> currentUserItemCategoryList;
    Hash mHash;
    public AddUserViewModel(@NonNull Application application) {
        super(application);
        PersonalFinanceDatabase instance = PersonalFinanceDatabase.getPersonalFinanceDatabase(application);

        UserDao userDao = instance.userDao();
        userRepository = new UserRepository(userDao);

        WalletDao walletDao = instance.walletDao();
        walletRepository = new WalletRepository(walletDao);

        ItemDao itemDao = instance.itemDao();
        itemRepository = new ItemRepository(itemDao);

        ItemCategoryDao itemCategoryDao = instance.itemCategoryDao();
        itemCategoryRepository = new ItemCategoryRepository(itemCategoryDao);

        mHash = Hash.getInstance();
    }

    public User getUser(String email) throws InterruptedException {
        class Foo implements Runnable{
            User user;
            @Override
            public void run() {
                user = userRepository.getUser(email);
            }

            public User getResult(){
                return user;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public void addUser(String firstName, String middleName, String lastName, String email, String password) throws InterruptedException {
        Thread thread = new Thread(() -> {
            String hashPass = null;
            try {
                hashPass = mHash.PBKDFHash(password);
                User user = new User(firstName, middleName, lastName, email, hashPass);
                userRepository.addUser(user);

                Integer userId = userRepository.getUserId(user.email);

                // Initialize some data for user
                initializeNewUser(userId);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.join();
    }

    public void initializeNewUser(Integer userId) throws InterruptedException {
        Thread thread = new Thread(() -> {
            // Add empty cash wallet
            CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
            Money money = Money.of(0, currencyUnit);
            Wallet cash = new Wallet(userId, "Cash", money);
            walletRepository.addWallet(cash);

            List<String> itemCategoryNameList = Arrays.asList("Food", "Entertainment", "Shopping", "Housing", "Transportation", "Maintenance", "Communication", "Investment", "Income", "Other");

            initializeItemCategory(userId, itemCategoryNameList);

            String[] foodItemInit = {"Bar", "Cafe", "Groceries", "Restaurant", "Fast-food"};
            String[] entertainmentItemInit = {"Toys", "Games", "Subscription", "Hobby"};
            String[] shoppingItemInit = {"Clothing", "Shoe", "Drug", "Electronic", "Gift", "Home appliance", "Accessory", "Kid", "Pet", "Tool"};
            String[] housingItemInit = {"Energy", "Water", "Rent", "Mortgage", "Insurance"};
            String[] transportationItemInit = {"Business trips", "Long distance", "Public transport", "Taxi"};
            String[] maintenanceItemInit = {"Vehicle", "Housing", "Equipment"};
            String[] communicationItemInit = {"Software", "Internet", "Phone and postal service"};
            String[] investmentItemInit = {"Collections", "Financial investment", "Realty", "Savings"};
            String[] incomeItemInit = {"Checks, coupons", "Child support", "Dues & grants", "Gifts", "Interests, dividends", "Lending, renting", "Lottery, gambling", "Refunds (tax, purchase)", "Rental income", "Sale", "Wage, invoices"};
            String[] otherItemInit = {"Other"};

            // Add some item
            try {
                initializeItem(userId, foodItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(0)));
                initializeItem(userId, entertainmentItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(1)));
                initializeItem(userId, shoppingItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(2)));
                initializeItem(userId, housingItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(3)));
                initializeItem(userId, transportationItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(4)));
                initializeItem(userId, maintenanceItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(5)));
                initializeItem(userId, communicationItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(6)));
                initializeItem(userId, investmentItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(7)));
                initializeItem(userId, incomeItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(8)));
                initializeItem(userId, otherItemInit, getCurrentUserItemCategoryId(userId, itemCategoryNameList.get(9)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        thread.join();
    }

    private void initializeItemCategory(Integer userId, List<String> itemCategoryNameList){
        for(String itemCategoryName: itemCategoryNameList){
            itemCategoryRepository.addItemCategory(new ItemCategory(userId, itemCategoryName));
        }
    }

    private void initializeItem(int userId, String[] itemNameList, Integer itemCategoryId){
        for(String itemName: itemNameList){
            itemRepository.addItem(new Item(userId, itemName, itemCategoryId));
        }
    }

    private Integer getCurrentUserItemCategoryId(Integer userId, String itemCategoryName) throws InterruptedException {
        class Foo implements Runnable{
            Integer result;
            @Override
            public void run() {
                result = itemCategoryRepository.getItemCategoryId(userId, itemCategoryName);
            }

            public Integer getResult(){
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
