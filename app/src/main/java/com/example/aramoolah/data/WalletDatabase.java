package com.example.aramoolah.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.aramoolah.converter.Converter;
import com.example.aramoolah.model.User;
import com.example.aramoolah.model.Wallet;

@Database(entities = {Wallet.class, User.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class WalletDatabase extends RoomDatabase {
    public abstract WalletDao walletDao();

    public volatile static WalletDatabase walletDatabase = null;

    public static WalletDatabase getWalletDatabase(Context context){
        if(walletDatabase == null){
            walletDatabase = Room.databaseBuilder(
                    context,
                    WalletDatabase.class,
                    "wallet_database"
            ).build();
        }
        return walletDatabase;
    }

}
