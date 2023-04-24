package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.repository.ItemRepository;

public class AddItemViewModel extends AndroidViewModel {
    ItemRepository itemRepository;
    public AddItemViewModel(@NonNull Application application) {
        super(application);

        ItemDao itemDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);
    }

    public void addItem(int userId, int itemCategoryId, String itemName) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Item item = new Item(userId, itemName, itemCategoryId);
                itemRepository.addItem(item);
            }
        });
        thread.start();
        thread.join();
    }
}
