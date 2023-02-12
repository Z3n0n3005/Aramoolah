package com.example.aramoolah.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table", indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public String password;
}
