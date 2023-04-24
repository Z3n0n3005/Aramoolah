package com.example.aramoolah.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_category_table",
    indices = {
        @Index(value = {"itemCategoryName"}),
        @Index(value = {"userId"})
    },
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    }
)
public class ItemCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemCategoryId")
    public Integer itemCategoryId;
    @ColumnInfo(name = "userId")
    public Integer userId;
    @ColumnInfo(name = "itemCategoryName")
    public String itemCategoryName;

    public ItemCategory(Integer userId, String itemCategoryName){
        this.userId = userId;
        this.itemCategoryName = itemCategoryName;
    }

    @NonNull
    @Override
    public String toString(){return itemCategoryName;}
}
