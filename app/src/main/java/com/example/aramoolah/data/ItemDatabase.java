package com.example.aramoolah.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.aramoolah.model.Item;

@Database(entities = {Item.class},version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
    public volatile static ItemDatabase itemDatabase = null;
    public ItemDatabase getItemDatabase(Context context){
        if(itemDatabase == null){
            itemDatabase = Room.databaseBuilder(
                    context,
                    ItemDatabase.class,
                    "item_database"
            ).build();
        }
        return itemDatabase;
    }
}
