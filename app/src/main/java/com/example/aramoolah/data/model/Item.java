package com.example.aramoolah.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

@Entity(tableName = "item_table", indices = {@Index(value = {"itemName"}, unique = true)},
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    }
)
public class Item{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    public Integer itemId;
    @ColumnInfo(name = "userId")
    public Integer userId;
    @ColumnInfo(name = "itemName")
    public String itemName;
    @ColumnInfo(name = "cost")
    public Money cost;
    @ColumnInfo(name = "itemCategory")
    public ItemCategory itemCategory;

    public Item(Integer userId, String itemName, Money cost, ItemCategory itemCategory){
        this.userId = userId;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.cost = cost;
    }
}
