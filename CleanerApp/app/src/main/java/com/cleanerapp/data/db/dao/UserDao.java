package com.cleanerapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanerapp.data.db.entities.UserEntity;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<UserEntity> getUserById(int id);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity login(String email, String password);

    @Update
    void updateUser(UserEntity user);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int emailExists(String email);
}
