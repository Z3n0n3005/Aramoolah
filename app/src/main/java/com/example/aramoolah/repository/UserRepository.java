package com.example.aramoolah.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.UserDao;
import com.example.aramoolah.data.UserDatabase;
import com.example.aramoolah.model.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    public UserRepository(UserDao userDao){
        this.userDao = userDao;
    }
    public void addUser(User user){
        userDao.addUser(user);
    }
    public void updateUser(User user){
        userDao.updateUser(user);
    }
    public void deleteUser(User user){
        userDao.deleteUser(user);
    }
    public LiveData<List<User>> getAllUser(){
        return userDao.getAllUser();
    }
    public int getUserId(String email){return userDao.getUserId(email);}
    public User getUser(String email){return userDao.getUser(email);}
}
