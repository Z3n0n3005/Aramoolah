package com.example.aramoolah.data.converter;

import androidx.room.TypeConverter;

import com.example.aramoolah.data.ItemCategory;
import com.example.aramoolah.data.model.TransactionType;

import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {
    // Transaction
    @TypeConverter
    public static String localDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return (localDateTime == null)? null : localDateTime.format(formatter);
    }

    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(String localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return (localDateTime == null)? null : LocalDateTime.parse(localDateTime, formatter);
    }

    @TypeConverter
    public static String transactionTypeToString(TransactionType transactionType){
        return (transactionType == null)? null : transactionType.toString();
    }

    @TypeConverter
    public static TransactionType stringToTransactionType(String transactionTypeStr){
        return (transactionTypeStr == null)? null : TransactionType.valueOf(transactionTypeStr);
    }

    // Item

    @TypeConverter
    public static String itemCategoryToString(ItemCategory itemCategory){
        return (itemCategory == null)? null : itemCategory.toString();
    }

    @TypeConverter
    public static ItemCategory stringToItemCategory(String itemCategoryStr){
        return (itemCategoryStr == null)? null : ItemCategory.valueOf(itemCategoryStr);
    }

    //Wallet

    @TypeConverter
    public static String moneyToString(Money money){
        return (money == null)? null : money.toString();
    }

    @TypeConverter
    public static Money stringToMoney(String money){
        return (money == null)? null : Money.parse(money);
    }

}
