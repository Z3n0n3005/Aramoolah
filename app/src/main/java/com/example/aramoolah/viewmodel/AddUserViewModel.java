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
    public AddUserViewModel(@NonNull Application application) {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);

        WalletDao walletDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).walletDao();
        walletRepository = new WalletRepository(walletDao);
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
                    hashPass = PBKDFHash(password);
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

    private String PBKDFHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
        class Foo implements Runnable {
            byte[] hash;
            byte[] salt;
            List<Byte> result;
            String resultStr;

            @Override
            public void run() {
                SecureRandom random = new SecureRandom();
                salt = new byte[16];
                random.nextBytes(salt);

                KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
                SecretKeyFactory factory = null;
                try {
                    factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                try {
                    hash = factory.generateSecret(spec).getEncoded();
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }

                String saltStr = "";
                for (byte s : salt) {
                    String st = String.format("%02X", s);
                    saltStr += st;
                }
                String hashStr = "";
                for(byte h: hash){
                    String st = String.format("%02X", h);
                    hashStr += st;
                }
                resultStr = hashStr + saltStr;
            }

            public String getResult(){
                return resultStr;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
}
