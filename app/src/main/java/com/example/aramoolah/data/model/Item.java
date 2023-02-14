package com.example.aramoolah.data.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

@Entity(tableName = "item_table", indices = {@Index(value = {"itemName"}, unique = true)})
public class Item{
    @PrimaryKey(autoGenerate = true)
    public Integer itemId;
    public String itemName;
    public Money cost;
    public ItemCategory itemCategory;

    public Item(String itemName, Money cost, ItemCategory itemCategory){
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.cost = cost;
    }
}
