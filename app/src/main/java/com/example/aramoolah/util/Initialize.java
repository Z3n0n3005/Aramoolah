package com.example.aramoolah.util;

import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;
import com.example.aramoolah.databinding.FragmentAddTransactionBinding;
import com.example.aramoolah.viewmodel.PersonalFinanceViewModel;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class Initialize {
    PersonalFinanceViewModel mPersonalFinanceViewModel;
    FragmentAddTransactionBinding binding;
    public Initialize(PersonalFinanceViewModel mPersonalFinanceViewModel, FragmentAddTransactionBinding binding) throws InterruptedException {
        this.mPersonalFinanceViewModel = mPersonalFinanceViewModel;
        this.binding = binding;
        addUser();
        addWallet();
        addItem();
        addTempTransaction();
        mPersonalFinanceViewModel.setCurrentUser("John@gmail.com");
    }

    public void addUser(){
        User user = new User("John","John","John","John@gmail.com","12345");
        mPersonalFinanceViewModel.addUser(user);
    }
    //
    public void addWallet() throws InterruptedException {
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Money money = Money.of(200000000, currencyUnit);

        Wallet wallet = new Wallet(mPersonalFinanceViewModel.getUserId("John@gmail.com"), "Cash", money);
        mPersonalFinanceViewModel.addWallet(wallet);
    }
    //
    public void addItem() throws InterruptedException {
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        Money redbull = Money.of(15000, currencyUnit);
        Integer userId = mPersonalFinanceViewModel.getUserId("John@gmail.com");

        Item item = new Item(userId, "redbull", redbull, ItemCategory.DRINK);
        mPersonalFinanceViewModel.addItem(item);

        Money cake = Money.of(20000, currencyUnit);

        Item item2 = new Item(userId, "cake", cake, ItemCategory.FOOD);
        mPersonalFinanceViewModel.addItem(item2);
    }
    public void addTempTransaction(){
        CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
        BigInteger amount = BigInteger.valueOf(15000);
        Money expense = Money.of(amount, currencyUnit);

        Transaction transaction = new Transaction(1, 1, TransactionType.EXPENSE, expense, 1, LocalDateTime.now());
        mPersonalFinanceViewModel.addTransaction(transaction, amount) ;
    }
}
