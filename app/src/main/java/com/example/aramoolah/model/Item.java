package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.javamoney.moneta.Money;

@Entity(tableName = "item_table", indices = {@Index(value = {"itemName"}, unique = true)})
public class Item{
    @PrimaryKey(autoGenerate = true)
    public int itemId;
    public String itemName;
    public Money cost;
    public ItemCategory itemCategory;
}
