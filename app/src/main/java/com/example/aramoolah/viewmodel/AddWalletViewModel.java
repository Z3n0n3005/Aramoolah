package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import org.javamoney.moneta.Money;

import java.math.BigInteger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddWalletViewModel extends PersonalFinanceViewModel{

    public AddWalletViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
    }

    @Override
    public void addWallet(String walletName, Long initialValue) {
        new Thread(() -> {
            CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
            BigInteger amount = BigInteger.valueOf(initialValue);
            Money initialMoney = Money.of(amount, currencyUnit);

            Wallet wallet = new Wallet(currentUser.userId, walletName, initialMoney);
            walletRepository.addWallet(wallet);
        }).start();

    }
}
