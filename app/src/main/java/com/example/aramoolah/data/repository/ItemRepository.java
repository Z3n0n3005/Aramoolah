package com.example.aramoolah.data.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.model.ItemCategory;

import java.util.List;
import java.util.Map;

public class ItemRepository {
    public ItemDao itemDao;
    public ItemRepository(ItemDao itemDao){
        this.itemDao = itemDao;
    }
    public void addItem(Item item){itemDao.addItem(item);}
    public void updateItem(Item item){itemDao.updateItem(item);}
    public void deleteItem(Item item){itemDao.deleteItem(item);}
    public LiveData<List<Item>> getAllItem(){return itemDao.getAllItem();}
    public int getItemId(String itemName){return itemDao.getItemId(itemName);}
    public Item getItem(Integer itemId){return itemDao.getItem(itemId);}
    public ItemCategory getItemCategory(String itemName){return itemDao.getItemCategory(itemName);}
    public Map<Integer, List<Integer>> getUserItemList(){return itemDao.getUserItemList();}
}
