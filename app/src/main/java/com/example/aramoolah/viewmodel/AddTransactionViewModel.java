package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;
import com.example.aramoolah.data.model.Wallet;

import org.javamoney.moneta.Money;

import java.math.BigInteger;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AddTransactionViewModel extends PersonalFinanceViewModel{
    public AddTransactionViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
    }

    @Override
    public void addTransaction(Transaction transaction, BigInteger cost){
        new Thread(() -> {
            BigInteger walletTotalAmount = BigInteger.valueOf(0);
            BigInteger updatedWalletTotalAmount = BigInteger.valueOf(0);
            for(Wallet wallet: Objects.requireNonNull(currentUserWalletList.getValue())){
                if(wallet.walletId.equals(transaction.walletId)){
                    walletTotalAmount = wallet.totalAmount.getNumberStripped().toBigInteger();
                    break;
                }
            }

            if(transaction.transactionType.equals(TransactionType.EXPENSE)) {
                updatedWalletTotalAmount = walletTotalAmount.subtract(cost.multiply(BigInteger.valueOf(transaction.numberOfItem)));
            }
            if(transaction.transactionType.equals(TransactionType.INCOME)){
                updatedWalletTotalAmount = walletTotalAmount.add(cost.multiply(BigInteger.valueOf(transaction.numberOfItem)));
            }

            CurrencyUnit currencyUnit = Monetary.getCurrency("VND");
            Money updatedWalletMoney = Money.of(updatedWalletTotalAmount, currencyUnit);

            transactionRepository.addTransaction(transaction);
            walletRepository.updateTotalAmount(transaction.walletId, updatedWalletMoney);
        }).start();
    }
}
