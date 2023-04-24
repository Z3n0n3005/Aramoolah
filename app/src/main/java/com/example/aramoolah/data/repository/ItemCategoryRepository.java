package com.example.aramoolah.data.repository;

import com.example.aramoolah.data.dao.ItemCategoryDao;
import com.example.aramoolah.data.model.ItemCategory;

import java.util.List;
import java.util.Map;

public class ItemCategoryRepository {
    public ItemCategoryDao itemCategoryDao;
    public ItemCategoryRepository(ItemCategoryDao itemCategoryDao){this.itemCategoryDao = itemCategoryDao;}
    public void addItemCategory(ItemCategory itemCategory){itemCategoryDao.addItemCategory(itemCategory);}
    public void updateItemCategory(ItemCategory itemCategory){itemCategoryDao.updateItemCategory(itemCategory);}
    public void deleteItemCategory(ItemCategory itemCategory){itemCategoryDao.deleteItemCategory(itemCategory);}
    public ItemCategory getItemCategory(Integer itemCategoryId){return itemCategoryDao.getItemCategory(itemCategoryId);}
    public Integer getItemCategoryId(Integer userId, String itemCategoryName){return itemCategoryDao.getItemCategoryId(userId, itemCategoryName);}
    public String getItemCategoryName(Integer itemCategoryId){return itemCategoryDao.getItemCategoryName(itemCategoryId);}
    public Map<Integer, List<Integer>> getUserItemCategory(){return itemCategoryDao.getUserItemCategoryList();}
}
