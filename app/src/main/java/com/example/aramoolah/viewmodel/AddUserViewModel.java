package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.ItemCategory;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.data.repository.ItemRepository;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.data.repository.WalletRepository;
import com.example.aramoolah.util.security.Hash;

import org.javamoney.moneta.Money;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddUserViewModel extends AndroidViewModel {
    UserRepository userRepository;
    WalletRepository walletRepository;
    ItemRepository itemRepository;
    Hash mHash;
    public AddUserViewModel(@NonNull Application application) {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);

        WalletDao walletDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);

        ItemDao itemDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);

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
            initializeItem(userId, foodItemInit, ItemCategory.FOOD);
            initializeItem(userId, entertainmentItemInit, ItemCategory.ENTERTAINMENT);
            initializeItem(userId, shoppingItemInit, ItemCategory.SHOPPING);
            initializeItem(userId, housingItemInit, ItemCategory.HOUSING);
            initializeItem(userId, transportationItemInit, ItemCategory.TRANSPORTATION);
            initializeItem(userId, maintenanceItemInit, ItemCategory.MAINTENANCE);
            initializeItem(userId, communicationItemInit, ItemCategory.COMMUNICATION);
            initializeItem(userId, investmentItemInit, ItemCategory.INVESTMENT);
            initializeItem(userId, incomeItemInit, ItemCategory.INCOME);
            initializeItem(userId, otherItemInit, ItemCategory.OTHER);
        });
        thread.start();
        thread.join();
    }

    private void initializeItem(int userId, String[] itemNameList, ItemCategory itemCategory){
        for(String itemName: itemNameList){
            itemRepository.addItem(new Item(userId, itemName, itemCategory));
        }
    }
}
