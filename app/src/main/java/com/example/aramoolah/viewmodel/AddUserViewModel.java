package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.data.repository.WalletRepository;
import com.example.aramoolah.util.security.Hash;

import org.javamoney.moneta.Money;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddUserViewModel extends AndroidViewModel {
    UserRepository userRepository;
    WalletRepository walletRepository;
    Hash mHash;
    public AddUserViewModel(@NonNull Application application) {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);

        WalletDao walletDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String hashPass = null;
                try {
                    hashPass = mHash.PBKDFHash(password);
                    User user = new User(firstName, middleName, lastName, email, hashPass);
                    userRepository.addUser(user);

                    // Initialize some data for user
                    CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
                    Money money = Money.of(0, currencyUnit);
                    Wallet cash = new Wallet(userRepository.getUserId(user.email), "Cash", money);
                    walletRepository.addWallet(cash);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        thread.join();
    }
}
