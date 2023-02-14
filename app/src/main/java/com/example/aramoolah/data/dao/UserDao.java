package com.example.aramoolah.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.aramoolah.data.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void addUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user_table ORDER BY userId")
    LiveData<List<User>> getAllUser();

    @Query("SELECT userId FROM user_table WHERE email = :email")
    int getUserId(String email);

    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUser(String email);
}
