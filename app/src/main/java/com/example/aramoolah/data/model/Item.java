package com.example.aramoolah.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.example.aramoolah.data.model.ItemCategory;

@Entity(tableName = "item_table",
        indices = {
            @Index(value = {"itemName"}),
            @Index(value = {"userId"}),
            @Index(value = {"itemCategoryId"})
        },
        foreignKeys = {
            @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
            @ForeignKey(entity = ItemCategory.class, parentColumns = "itemCategoryId", childColumns = "itemCategoryId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        }
)
public class Item{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    public Integer itemId;
    @ColumnInfo(name = "userId")
    public Integer userId;
    @ColumnInfo(name = "itemCategoryId")
    public Integer itemCategoryId;
    @ColumnInfo(name = "itemName")
    public String itemName;

    public Item(Integer userId, String itemName, Integer itemCategoryId){
        this.userId = userId;
        this.itemCategoryId = itemCategoryId;
        this.itemName = itemName;
    }

    @NonNull
    @Override
    public String toString() {
        return itemName;
    }
}
