package com.cleanerapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanerapp.data.db.entities.BookingEntity;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBooking(BookingEntity booking);

    @Query("SELECT * FROM bookings WHERE user_id = :userId ORDER BY created_at DESC")
    LiveData<List<BookingEntity>> getBookingsByUser(int userId);

    @Query("SELECT * FROM bookings WHERE cleaner_id = :cleanerId ORDER BY created_at DESC")
    LiveData<List<BookingEntity>> getBookingsByCleaner(int cleanerId);

    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    void updateBookingStatus(int bookingId, String status);

    @Query("SELECT COUNT(*) FROM bookings WHERE cleaner_id = :cleanerId AND status = 'PENDING'")
    int getPendingBookingCount(int cleanerId);
}
