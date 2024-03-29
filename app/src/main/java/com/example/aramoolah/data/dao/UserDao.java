package com.example.aramoolah.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.data.model.User;

import java.util.List;
import java.util.Map;

@Dao
public interface UserDao {
    @Insert
    void addUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_table ORDER BY userId")
    List<User> getAllUser();

    @Query("SELECT userId FROM user_table WHERE email = :email")
    int getUserId(String email);

    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUser(String email);

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    User getUser(Integer userId);

    @MapInfo(keyColumn = "userId", valueColumn = "walletId")
    @Query("SELECT * FROM user_table u JOIN wallet_table w ON u.userId = w.userId")
    Map<Integer, List<Integer>> getCurrentUserWalletList();

    @MapInfo(keyColumn = "userId", valueColumn = "transactionId")
    @Query("SELECT * FROM user_table u JOIN wallet_table w ON u.userId = w.userId JOIN transaction_table t ON t.walletId = w.walletId")
    Map<Integer, List<Integer>> getCurrentUserTransactionList();



}
