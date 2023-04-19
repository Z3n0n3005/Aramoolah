package com.example.aramoolah.data.repository;

import androidx.lifecycle.LiveData;

import com.example.aramoolah.data.dao.UserDao;
import com.example.aramoolah.data.model.User;

import java.util.List;
import java.util.Map;

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
    public List<User> getAllUser(){
        return userDao.getAllUser();
    }
    public int getUserId(String email){return userDao.getUserId(email);}
    public User getUser(String email){return userDao.getUser(email);}
    public User getUser(Integer userId){return userDao.getUser(userId);}
    public Map<Integer, List<Integer>> getCurrentUserWalletList(){return userDao.getCurrentUserWalletList();}
    public Map<Integer, List<Integer>> getCurrentUserTransactionList(){return userDao.getCurrentUserTransactionList();}
}
