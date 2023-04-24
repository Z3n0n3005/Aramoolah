package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.aramoolah.data.dao.ItemCategoryDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.ItemCategory;
import com.example.aramoolah.data.repository.ItemCategoryRepository;

public class AddItemCategoryViewModel extends AndroidViewModel {
    ItemCategoryRepository itemCategoryRepository;
    public AddItemCategoryViewModel(@NonNull Application application) {
        super(application);

        ItemCategoryDao itemCategoryDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).itemCategoryDao();
        itemCategoryRepository = new ItemCategoryRepository(itemCategoryDao);
    }

    public void addItemCategory(Integer userId, String itemCategoryName) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ItemCategory itemCategory = new ItemCategory(userId, itemCategoryName);
                itemCategoryRepository.addItemCategory(itemCategory);
            }
        });
        thread.start();
        thread.join();
    }
}
