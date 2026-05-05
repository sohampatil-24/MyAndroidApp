package com.cleanerapp.di;

import com.cleanerapp.data.db.dao.BookingDao;
import com.cleanerapp.data.db.dao.CleanerServiceDao;
import com.cleanerapp.data.db.dao.ServiceCategoryDao;
import com.cleanerapp.data.db.dao.UserDao;
import com.cleanerapp.data.repository.BookingRepository;
import com.cleanerapp.data.repository.CleanerRepository;
import com.cleanerapp.data.repository.UserRepository;
import com.cleanerapp.utils.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserDao userDao) {
        return new UserRepository(userDao);
    }

    @Provides
    @Singleton
    public CleanerRepository provideCleanerRepository(
            CleanerServiceDao cleanerServiceDao,
            ServiceCategoryDao serviceCategoryDao) {
        return new CleanerRepository(cleanerServiceDao, serviceCategoryDao);
    }

    @Provides
    @Singleton
    public BookingRepository provideBookingRepository(BookingDao bookingDao) {
        return new BookingRepository(bookingDao);
    }
}
