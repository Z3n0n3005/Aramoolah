package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item{
    @PrimaryKey(autoGenerate = true)
    public int itemId;
    public String name;
    public int cost;
    public ItemCategory itemCategory;
}
