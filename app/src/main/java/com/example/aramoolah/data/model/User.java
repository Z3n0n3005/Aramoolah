package com.example.aramoolah.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table", indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    public Integer userId;
    @ColumnInfo(name = "firstName")
    public String firstName;
    @ColumnInfo(name = "middleName")
    public String middleName;
    @ColumnInfo(name = "lastName")
    public String lastName;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "password")
    public String password;

    public User(String firstName, String middleName, String lastName, String email, String password){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
