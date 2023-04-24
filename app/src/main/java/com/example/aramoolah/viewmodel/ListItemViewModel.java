package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.ItemDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.Item;
import com.example.aramoolah.data.ItemCategory;
import com.example.aramoolah.data.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ListItemViewModel extends PersonalFinanceViewModel{
    ItemRepository itemRepository;


    public ListItemViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        ItemDao itemDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).itemDao();
        itemRepository = new ItemRepository(itemDao);
    }

//    public LiveData<List<Item>> getItemList(ItemCategory itemCategory) throws InterruptedException {
//        class Foo implements Runnable{
//            List<Item> currentUserItemList;
//            List<Item> result = new ArrayList<>();
//            MutableLiveData<List<Item>> lResult;
//            @Override
//            public void run() {
//                try {
//                    currentUserItemList = getCurrentUserItemList().getValue();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                assert currentUserItemList != null;
//                for(Item item: currentUserItemList){
//                    if(item.itemCategory.equals(itemCategory)){
//                        result.add(item);
//                    }
//                }
//                lResult = new MutableLiveData<>(result);
//            }
//
//            public LiveData<List<Item>> getResult(){
//                return lResult;
//            }
//        }
//        Foo foo = new Foo();
//        Thread thread = new Thread(foo);
//        thread.start();
//        thread.join();
//        return foo.getResult();
//    }
}
