package com.example.aramoolah.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.ItemDao;
import com.example.aramoolah.model.Item;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    public ItemRepository(ItemDao itemDao){
        this.itemDao = itemDao;
    }
    public void addItem(Item item){itemDao.addItem(item);}
    public void updateItem(Item item){itemDao.updateItem(item);}
    public void deleteItem(Item item){itemDao.deleteItem(item);}
    public LiveData<List<Item>> getAllItem(){return itemDao.getAllItem();}
}