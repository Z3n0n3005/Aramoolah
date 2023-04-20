package com.example.aramoolah.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.database.PersonalFinanceDatabase;
import com.example.aramoolah.data.model.User;
import com.example.aramoolah.data.repository.UserRepository;
import com.example.aramoolah.util.security.Hash;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginViewModel extends AndroidViewModel {
    UserRepository userRepository;
    volatile MutableLiveData<List<User>> userList;
    MutableLiveData<User> currentUser;
    Hash mHash;
    public LoginViewModel(@NonNull Application application) throws InterruptedException {
        super(application);
        UserDao userDao = PersonalFinanceDatabase.getPersonalFinanceDatabase(application).userDao();
        userRepository = new UserRepository(userDao);
        userList = getUserList();
        currentUser = new MutableLiveData<>();
        mHash = Hash.getInstance();
    }

    public MutableLiveData<List<User>> getUserList() throws InterruptedException {
        class Foo implements Runnable {
            private volatile MutableLiveData<List<User>> mUserList = new MutableLiveData<>();

            @Override
            public void run() {
                if(userList == null) {
                    mUserList = new MutableLiveData<>(userRepository.getAllUser());
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

    public void updateUserList() throws InterruptedException {
        Thread thread = new Thread(() -> userList.postValue(userRepository.getAllUser()));
        thread.start();
        thread.join();
    }

    public void setCurrentUser(int userId) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userRepository.getUser(userId);
                currentUser.postValue(user);
            }
        });
        thread.start();
        thread.join();
    }
    public LiveData<User> getCurrentUser(){
        return currentUser;
    }

    public boolean isCorrectPassword(Integer userId, String passwordProvided) throws InterruptedException {
        class Foo implements Runnable{
            Boolean result = false;
            @Override
            public void run() {
                String password = Objects.requireNonNull(currentUser.getValue()).password;
                int toInd = password.length();
                int fromInd = toInd - 32;
                String saltStrRetrieved = password.substring(fromInd, toInd);
                try {
                    byte[] retrievedSalt = mHash.getSaltFromPassword(saltStrRetrieved);
                    String passwordProvidedHash = mHash.PBKDFHash(passwordProvided, retrievedSalt);
                    if(password.equals(passwordProvidedHash)){
                        result = true;
                    }
                } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            }

            public boolean getResult(){return result;}
        }
        Foo foo = new Foo();
        Thread thread = new Thread(foo);
        thread.start();
        thread.join();
        return foo.getResult();
    }

}
