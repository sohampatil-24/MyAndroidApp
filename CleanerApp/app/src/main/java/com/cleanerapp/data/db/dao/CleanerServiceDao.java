package com.cleanerapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanerapp.data.db.entities.CleanerServiceEntity;

import java.util.List;

@Dao
public interface CleanerServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertService(CleanerServiceEntity service);

    @Update
    void updateService(CleanerServiceEntity service);

    @Query("SELECT * FROM cleaner_services WHERE is_available = 1 ORDER BY created_at DESC")
    LiveData<List<CleanerServiceEntity>> getAllAvailableServices();

    @Query("SELECT * FROM cleaner_services WHERE service_type LIKE '%' || :query || '%' AND is_available = 1")
    LiveData<List<CleanerServiceEntity>> searchServices(String query);

    @Query("SELECT * FROM cleaner_services WHERE service_type = :type AND is_available = 1 ORDER BY created_at DESC")
    LiveData<List<CleanerServiceEntity>> getServicesByType(String type);

    @Query("SELECT * FROM cleaner_services WHERE user_id = :userId ORDER BY created_at DESC")
    LiveData<List<CleanerServiceEntity>> getServicesByUser(int userId);

    @Query("UPDATE cleaner_services SET is_available = :available WHERE id = :serviceId")
    void updateAvailability(int serviceId, boolean available);

    @Query("DELETE FROM cleaner_services WHERE id = :serviceId")
    void deleteService(int serviceId);

    @Query("SELECT * FROM cleaner_services WHERE id = :serviceId LIMIT 1")
    CleanerServiceEntity getServiceById(int serviceId);
}
