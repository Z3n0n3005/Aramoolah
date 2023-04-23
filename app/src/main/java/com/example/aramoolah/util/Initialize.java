package com.example.aramoolah.util;

import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.viewmodel.AddTransactionViewModel;
import com.example.aramoolah.viewmodel.LoginViewModel;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class Initialize {
    // TODO: Recreate Initialize
    AddTransactionViewModel mAddTransactionViewModel;
    LoginViewModel mLoginViewModel;
    public Initialize(AddTransactionViewModel mAddTransactionViewModel, LoginViewModel mLoginViewModel) throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.mAddTransactionViewModel = mAddTransactionViewModel;
        this.mLoginViewModel = mLoginViewModel;

        addUser();
        addWallet();
        addItem();
        addTempTransaction();
        mAddTransactionViewModel.setCurrentUser("John@gmail.com");
    }

    public void addUser() throws NoSuchAlgorithmException, InvalidKeySpecException, InterruptedException {
//        User user = new User("John","John","John","John@gmail.com", mLoginViewModel.m.PBKDFHash("12345"));
//        mAddTransactionViewModel.addUser(user);
    }
    //
    public void addWallet() throws InterruptedException {
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Money money = Money.of(200000000, currencyUnit);

        Wallet wallet = new Wallet(mAddTransactionViewModel.getUserId("John@gmail.com"), "Cash", money);
        mAddTransactionViewModel.addWallet(wallet);
    }
    //
    public void addItem() throws InterruptedException {
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Money redbull = Money.of(15000, currencyUnit);
        Integer userId = mAddTransactionViewModel.getUserId("John@gmail.com");

//        Item item = new Item(userId, "redbull", redbull, ItemCategory.DRINK);
//        mAddTransactionViewModel.addItem(item);

//        Money cake = Money.of(20000, currencyUnit);

//        Item item2 = new Item(userId, "cake", cake, ItemCategory.FOOD);
//        mAddTransactionViewModel.addItem(item2);
    }
    public void addTempTransaction(){
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        BigInteger amount = BigInteger.valueOf(15000);
        Money expense = Money.of(amount, currencyUnit);

        Transaction transaction = new Transaction(1, 1, TransactionType.EXPENSE, expense, 1, LocalDateTime.now());
        mAddTransactionViewModel.addTransaction(transaction, amount) ;
    }
}
