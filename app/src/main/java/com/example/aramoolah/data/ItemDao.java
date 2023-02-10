package com.example.aramoolah.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.model.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Update
    void updateItem(Item item);

    @Insert
    void addItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * FROM item_table ORDER BY item_table.itemId")
    LiveData<List<Item>> getAllItem();
}
