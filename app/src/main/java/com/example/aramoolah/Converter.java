package com.example.aramoolah;

import androidx.room.TypeConverter;

import com.example.aramoolah.model.ItemCategory;
import com.example.aramoolah.model.TransactionType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {
    // Transaction
    @TypeConverter
    public static String localDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return localDateTime.format(formatter);
    }

    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(String localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        return LocalDateTime.parse(localDateTime, formatter);
    }

    @TypeConverter
    public static String transactionTypeToString(TransactionType transactionType){
        return transactionType.toString();
    }

    @TypeConverter
    public static TransactionType stringToTransactionType(String transactionTypeStr){
        return TransactionType.valueOf(transactionTypeStr);
    }

    // Item

    @TypeConverter
    public static String itemCategoryToString(ItemCategory itemCategory){
        return itemCategory.toString();
    }

    @TypeConverter
    public static ItemCategory stringToItemCategory(String itemCategoryStr){
        return ItemCategory.valueOf(itemCategoryStr);
    }


}
