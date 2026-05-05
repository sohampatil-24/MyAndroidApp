package com.cleanerapp.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanerapp.data.db.dao.BookingDao;
import com.cleanerapp.data.db.dao.CleanerServiceDao;
import com.cleanerapp.data.db.dao.ServiceCategoryDao;
import com.cleanerapp.data.db.dao.UserDao;
import com.cleanerapp.data.db.entities.BookingEntity;
import com.cleanerapp.data.db.entities.CleanerServiceEntity;
import com.cleanerapp.data.db.entities.ServiceCategoryEntity;
import com.cleanerapp.data.db.entities.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {UserEntity.class, CleanerServiceEntity.class, BookingEntity.class, ServiceCategoryEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public abstract UserDao userDao();
    public abstract CleanerServiceDao cleanerServiceDao();
    public abstract BookingDao bookingDao();
    public abstract ServiceCategoryDao serviceCategoryDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cleaner_app_database"
                    )
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Pre-populate service categories on first run
                            databaseExecutor.execute(() -> {
                                AppDatabase database = getInstance(context);
                                seedDefaultCategories(database);
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedDefaultCategories(AppDatabase db) {
        List<ServiceCategoryEntity> defaultCategories = Arrays.asList(
            new ServiceCategoryEntity("Car Cleaning", "ic_car"),
            new ServiceCategoryEntity("Home Cleaning", "ic_home"),
            new ServiceCategoryEntity("Apartment Cleaning", "ic_apartment"),
            new ServiceCategoryEntity("Office Cleaning", "ic_office"),
            new ServiceCategoryEntity("Glass Cleaning", "ic_glass"),
            new ServiceCategoryEntity("Deep Cleaning", "ic_deep"),
            new ServiceCategoryEntity("Sofa Cleaning", "ic_sofa")
        );
        db.serviceCategoryDao().insertCategories(defaultCategories);
    }
}
