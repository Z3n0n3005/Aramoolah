package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class Item{
    @PrimaryKey(autoGenerate = true)
    int itemId;
    String name;
    int cost;
    ItemCategory itemCategory;
}
