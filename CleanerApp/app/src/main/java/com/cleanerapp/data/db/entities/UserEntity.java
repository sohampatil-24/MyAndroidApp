package com.cleanerapp.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = "email", unique = true)})
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "role")
    public String role; // "USER" or "CLEANER"

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public UserEntity() {}

    public UserEntity(String name, String email, String password, String phone, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }
}
