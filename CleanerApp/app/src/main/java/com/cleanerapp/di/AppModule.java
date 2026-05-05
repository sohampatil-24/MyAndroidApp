package com.cleanerapp.di;

import android.content.Context;

import com.cleanerapp.data.db.AppDatabase;
import com.cleanerapp.data.db.dao.BookingDao;
import com.cleanerapp.data.db.dao.CleanerServiceDao;
import com.cleanerapp.data.db.dao.ServiceCategoryDao;
import com.cleanerapp.data.db.dao.UserDao;
import com.cleanerapp.utils.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }

    @Provides
    @Singleton
    public CleanerServiceDao provideCleanerServiceDao(AppDatabase database) {
        return database.cleanerServiceDao();
    }

    @Provides
    @Singleton
    public BookingDao provideBookingDao(AppDatabase database) {
        return database.bookingDao();
    }

    @Provides
    @Singleton
    public ServiceCategoryDao provideServiceCategoryDao(AppDatabase database) {
        return database.serviceCategoryDao();
    }

    @Provides
    @Singleton
    public SessionManager provideSessionManager(@ApplicationContext Context context) {
        return new SessionManager(context);
    }
}
