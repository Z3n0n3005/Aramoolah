package com.example.aramoolah.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table", indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public Integer userId;
    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public String password;

    public User(String firstName, String middleName, String lastName, String email, String password){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
