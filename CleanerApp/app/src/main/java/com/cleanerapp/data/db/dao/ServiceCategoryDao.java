package com.cleanerapp.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanerapp.data.db.entities.ServiceCategoryEntity;

import java.util.List;

@Dao
public interface ServiceCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategory(ServiceCategoryEntity category);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategories(List<ServiceCategoryEntity> categories);

    @Query("SELECT * FROM service_categories WHERE is_active = 1 ORDER BY name ASC")
    LiveData<List<ServiceCategoryEntity>> getAllActiveCategories();

    @Query("SELECT * FROM service_categories WHERE is_active = 1 ORDER BY name ASC")
    List<ServiceCategoryEntity> getAllCategoriesSync();

    @Query("SELECT COUNT(*) FROM service_categories")
    int getCategoryCount();
}
