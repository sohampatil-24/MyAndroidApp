package com.cleanerapp.data.repository;

import androidx.lifecycle.LiveData;

import com.cleanerapp.data.db.AppDatabase;
import com.cleanerapp.data.db.dao.BookingDao;
import com.cleanerapp.data.db.entities.BookingEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BookingRepository {

    private final BookingDao bookingDao;
    private final ExecutorService executor;

    @Inject
    public BookingRepository(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
        this.executor = AppDatabase.databaseExecutor;
    }

    public void createBooking(BookingEntity booking, UserRepository.RepositoryCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                long result = bookingDao.insertBooking(booking);
                callback.onResult(result > 0);
            } catch (Exception e) {
                callback.onResult(false);
            }
        });
    }

    public LiveData<List<BookingEntity>> getBookingsByUser(int userId) {
        return bookingDao.getBookingsByUser(userId);
    }

    public LiveData<List<BookingEntity>> getBookingsByCleaner(int cleanerId) {
        return bookingDao.getBookingsByCleaner(cleanerId);
    }

    public void updateStatus(int bookingId, String status) {
        executor.execute(() -> bookingDao.updateBookingStatus(bookingId, status));
    }
}
