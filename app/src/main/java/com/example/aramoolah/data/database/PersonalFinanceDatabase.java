package com.example.aramoolah.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aramoolah.data.converter.Converter;
import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.dao.TransactionDao;
import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.dao.WalletDao;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.Transaction;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.model.Wallet;

@Database(entities = {Transaction.class, Wallet.class, Item.class, User.class}, version = 7)
@TypeConverters({Converter.class})
public abstract class PersonalFinanceDatabase extends RoomDatabase {
    public volatile static PersonalFinanceDatabase personalFinanceDatabase = null;
    public abstract TransactionDao transactionDao();
    public abstract WalletDao walletDao();
    public abstract ItemDao itemDao();
    public abstract UserDao userDao();

    public static PersonalFinanceDatabase getTransactionDatabase(Context context){
        if(personalFinanceDatabase == null){
            personalFinanceDatabase = Room.databaseBuilder(
                    context,
                    PersonalFinanceDatabase.class,
                    "personal_finance_database"
            ).fallbackToDestructiveMigration().build();
        }
        return personalFinanceDatabase;
    }
}
