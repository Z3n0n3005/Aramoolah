package com.example.aramoolah.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aramoolah.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public volatile static UserDatabase userDatabase = null;

    public static UserDatabase getUserDatabase(Context context){
        if(userDatabase == null){
            userDatabase = Room.databaseBuilder(
                    context,
                    UserDatabase.class,
                    "user_database"
            ).build();
        }
        return userDatabase;
    }
}
