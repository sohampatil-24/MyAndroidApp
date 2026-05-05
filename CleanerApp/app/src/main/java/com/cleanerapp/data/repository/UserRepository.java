package com.cleanerapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanerapp.data.db.AppDatabase;
import com.cleanerapp.data.db.dao.UserDao;
import com.cleanerapp.data.db.entities.UserEntity;
import com.cleanerapp.model.AuthResult;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final UserDao userDao;
    private final ExecutorService executor;

    @Inject
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
        this.executor = AppDatabase.databaseExecutor;
    }

    public void register(UserEntity user, RepositoryCallback<AuthResult> callback) {
        executor.execute(() -> {
            try {
                int count = userDao.emailExists(user.email);
                if (count > 0) {
                    callback.onResult(AuthResult.error("Email already registered"));
                    return;
                }
                long id = userDao.insertUser(user);
                if (id > 0) {
                    user.id = (int) id;
                    callback.onResult(AuthResult.success(user));
                } else {
                    callback.onResult(AuthResult.error("Registration failed. Please try again."));
                }
            } catch (Exception e) {
                callback.onResult(AuthResult.error("Error: " + e.getMessage()));
            }
        });
    }

    public void login(String email, String password, RepositoryCallback<AuthResult> callback) {
        executor.execute(() -> {
            try {
                UserEntity user = userDao.login(email.trim(), password);
                if (user != null) {
                    callback.onResult(AuthResult.success(user));
                } else {
                    callback.onResult(AuthResult.error("Invalid email or password"));
                }
            } catch (Exception e) {
                callback.onResult(AuthResult.error("Login error: " + e.getMessage()));
            }
        });
    }

    public LiveData<UserEntity> getUserById(int id) {
        return userDao.getUserById(id);
    }

    public interface RepositoryCallback<T> {
        void onResult(T result);
    }
}
