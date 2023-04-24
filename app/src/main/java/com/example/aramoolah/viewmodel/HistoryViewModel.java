package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.TransactionType;

import java.math.BigInteger;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HistoryViewModel extends PersonalFinanceViewModel{
    protected MutableLiveData<Map<YearMonth, BigInteger>> mapMonthToMoney;
    protected MutableLiveData<Map<Long, String>> mapTransactionIdToItemCategoryName;

    public HistoryViewModel(@NonNull Application application) throws InterruptedException {
        super(application);

    }

    public MutableLiveData<Map<YearMonth, BigInteger>> getMapMonthToMoney() throws InterruptedException {
        class Foo implements Runnable{
            MutableLiveData<Map<YearMonth, BigInteger>> result;

            @Override
            public void run() {
                if(mapMonthToMoney == null) {
                    YearMonth prevMonth = null;
                    BigInteger currentSum = BigInteger.ZERO;
                    Map<YearMonth, BigInteger> hashMapMonthToMoney = new HashMap<>();

                    // Add entries into hashmap mapMonthToMoney
                    List<Transaction> transactionList = null;
                    try {
                        transactionList = getCurrentUserTransactionList().getValue();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(transactionList == null) {
                        return;
                    }
                    for (int ind = 0; ind <= transactionList.size(); ind++) {
                        if(ind == transactionList.size()){
                            hashMapMonthToMoney.put(prevMonth, currentSum);
                            break;
                        }

                        Transaction transaction = transactionList.get(ind);
                        YearMonth currentMonth = YearMonth.from(transaction.localDateTime);
                        BigInteger currentAmountOfMoney = BigInteger.ZERO;
                        TransactionType transactionType = transaction.transactionType;
                        if (transactionType == TransactionType.INCOME){
                            currentAmountOfMoney = transaction.amountOfMoney.getNumberStripped().toBigInteger();
                        } else if(transactionType == TransactionType.EXPENSE){
                            currentAmountOfMoney = BigInteger.ZERO.subtract(transaction.amountOfMoney.getNumberStripped().toBigInteger());
                        }
                        // prevMonth is null only at start of transactionList
                        if (prevMonth == null || currentMonth.equals(prevMonth)) {
                            currentSum = currentSum.add(currentAmountOfMoney);
                        } else {
                            hashMapMonthToMoney.put(prevMonth, currentSum);
                            currentSum = currentAmountOfMoney;
                        }
                        prevMonth = currentMonth;
                    }

                    result = new MutableLiveData<>(hashMapMonthToMoney);
                } else {
                    result = mapMonthToMoney;
                }
            }

            public MutableLiveData<Map<YearMonth, BigInteger>> getResult(){return result;}
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

    public MutableLiveData<Map<Long, String>> getMapTransactionIdToItemCategoryName() throws InterruptedException {
        class Foo implements Runnable{
            MutableLiveData<Map<Long, String>> result;
            @Override
            public void run() {
                if(mapTransactionIdToItemCategoryName == null){
                    try {
                        Map<Long, String> mapTransactionToItemCategoryName = new HashMap<>();
                        List<Transaction> transactionList = getCurrentUserTransactionList().getValue();
                        List<Item> itemList = new ArrayList<>();
                        for(Transaction transaction: transactionList){
                            itemList.add(getItem(transaction.itemId));
                        }
                        for(int i = 0; i < transactionList.size(); i++){
                            Transaction transaction = transactionList.get(i);
                            ItemCategory itemCategory = getItemCategory(itemList.get(i).itemCategoryId);
                            mapTransactionToItemCategoryName.put(transaction.transactionId,itemCategory.itemCategoryName);
                        }
                        result = new MutableLiveData<>(mapTransactionToItemCategoryName);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    result = mapTransactionIdToItemCategoryName;
                }
            }

            public MutableLiveData<Map<Long, String>> getResult(){
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
