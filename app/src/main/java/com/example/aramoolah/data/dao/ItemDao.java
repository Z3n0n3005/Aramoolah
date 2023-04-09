package com.example.aramoolah.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;

import java.util.List;
import java.util.Map;

@Dao
public interface ItemDao {
    @Update
    void updateItem(Item item);

    @Insert
    void addItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * FROM item_table ORDER BY itemId")
    LiveData<List<Item>> getAllItem();

    @Query("SELECT itemId FROM item_table WHERE itemName = :itemName")
    int getItemId(String itemName);

    @Query("SELECT * FROM item_table i WHERE i.itemId = :itemId")
    Item getItem(Integer itemId);

    @Query("SELECT itemCategory FROM item_table WHERE itemName = :itemName")
    ItemCategory getItemCategory(String itemName);

    @MapInfo(keyColumn = "userId", valueColumn = "itemId")
    @Query("SELECT * FROM item_table i JOIN user_table u ON i.userId = u.userId")
    Map<Integer, List<Integer>> getUserItemList();


}
