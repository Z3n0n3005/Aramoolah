package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.repository.UserRepository;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    UserRepository userRepository;
    MutableLiveData<List<User>> userList;
    public LoginViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);
        userList = getUserList();
    }

    public MutableLiveData<List<User>> getUserList() throws InterruptedException {
        class Foo implements Runnable {
            MutableLiveData<List<User>> mUserList;

            @Override
            public void run() {
                if(userList == null) {
                    List<User> userList = userRepository.getAllUser();
                    mUserList = new MutableLiveData<>(userList);
                } else {
                    mUserList = userList;
                }
            }

            public MutableLiveData<List<User>> getResult(){
                return mUserList;
            }
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }
}
