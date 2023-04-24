package com.example.aramoolah.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.data.model.ItemCategory;

import java.util.List;
import java.util.Map;
@Dao
public interface ItemCategoryDao {
    @Update
    void updateItemCategory(ItemCategory itemCategory);
    @Insert
    void addItemCategory(ItemCategory itemCategory);
    @Delete
    void deleteItemCategory(ItemCategory itemCategory);

    @Query("SELECT * FROM item_category_table WHERE itemCategoryId = :itemCategoryId")
    ItemCategory getItemCategory(Integer itemCategoryId);

    @Query("SELECT itemCategoryName FROM item_category_table WHERE itemCategoryId = :itemCategoryId")
    String getItemCategoryName(Integer itemCategoryId);
    @Query("SELECT itemCategoryId FROM item_category_table WHERE userId = :userId AND itemCategoryName = :itemCategoryName")
    Integer getItemCategoryId(Integer userId, String itemCategoryName);

    @MapInfo(keyColumn = "userId", valueColumn = "itemCategoryId")
    @Query("SELECT * FROM item_category_table ic JOIN user_table u ON ic.userId = u.userId")
    Map<Integer, List<Integer>> getUserItemCategoryList();
}
