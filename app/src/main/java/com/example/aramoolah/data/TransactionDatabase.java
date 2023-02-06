package com.example.aramoolah.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aramoolah.Converter;
import com.example.aramoolah.model.Transaction;

@Database(entities = {Transaction.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class TransactionDatabase extends RoomDatabase {
    public volatile static TransactionDatabase transactionDatabase = null;
    public abstract TransactionDao transactionDao();

    public static TransactionDatabase getTransactionDatabase(Context context){
        if(transactionDatabase == null){
            transactionDatabase = Room.databaseBuilder(
                    context,
                    TransactionDatabase.class,
                    "transaction_database"
            ).build();
        }
        return transactionDatabase;
    }
}