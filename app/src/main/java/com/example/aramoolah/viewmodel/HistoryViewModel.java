package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.model.Transaction;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HistoryViewModel extends PersonalFinanceViewModel{
    protected MutableLiveData<Map<String, BigInteger>> mapMonthToMoney;

    public HistoryViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        mapMonthToMoney = getMapMonthToMoney();

    }

    public MutableLiveData<Map<String, BigInteger>> getMapMonthToMoney() throws InterruptedException {
        class Foo implements Runnable{
            MutableLiveData<Map<String, BigInteger>> result;

            @Override
            public void run() {
                if(mapMonthToMoney == null) {
                    String prevMonth = "";
                    BigInteger currentSum = BigInteger.ZERO;
                    Map<String, BigInteger> hashMapMonthToMoney = new HashMap<>();

                    // Add entries into hashmap mapMonthToMoney
                    List<Transaction> transactionList = null;
                    try {
                        transactionList = getCurrentUserTransactionList().getValue();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(transactionList != null) {
                        for (Transaction transaction : transactionList) {
                            String currentMonth = transaction.localDateTime.getMonth().toString();
                            BigInteger currentAmountOfMoney = transaction.amountOfMoney.getNumberStripped().toBigInteger();

                            if (currentMonth.equals("") || currentMonth.equals(prevMonth)) {
                                currentSum = currentSum.add(currentAmountOfMoney);
                            } else {
                                hashMapMonthToMoney.put(prevMonth, currentSum);
                                currentSum = currentAmountOfMoney;
                            }
                            prevMonth = currentMonth;
                        }
                    }

                    result = new MutableLiveData<>(hashMapMonthToMoney);
                } else {
                    result = mapMonthToMoney;
                }
            }

            public MutableLiveData<Map<String, BigInteger>> getResult(){return result;}
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
}
