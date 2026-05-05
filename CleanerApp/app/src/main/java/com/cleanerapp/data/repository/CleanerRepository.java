package com.cleanerapp.data.repository;

import androidx.lifecycle.LiveData;

import com.cleanerapp.data.db.AppDatabase;
import com.cleanerapp.data.db.dao.CleanerServiceDao;
import com.cleanerapp.data.db.dao.ServiceCategoryDao;
import com.cleanerapp.data.db.entities.CleanerServiceEntity;
import com.cleanerapp.data.db.entities.ServiceCategoryEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CleanerRepository {

    private final CleanerServiceDao cleanerServiceDao;
    private final ServiceCategoryDao serviceCategoryDao;
    private final ExecutorService executor;

    @Inject
    public CleanerRepository(CleanerServiceDao cleanerServiceDao, ServiceCategoryDao serviceCategoryDao) {
        this.cleanerServiceDao = cleanerServiceDao;
        this.serviceCategoryDao = serviceCategoryDao;
        this.executor = AppDatabase.databaseExecutor;
    }

    public LiveData<List<CleanerServiceEntity>> getAllAvailableServices() {
        return cleanerServiceDao.getAllAvailableServices();
    }

    public LiveData<List<CleanerServiceEntity>> searchServices(String query) {
        return cleanerServiceDao.searchServices(query);
    }

    public LiveData<List<CleanerServiceEntity>> getServicesByType(String type) {
        return cleanerServiceDao.getServicesByType(type);
    }

    public LiveData<List<CleanerServiceEntity>> getServicesByUser(int userId) {
        return cleanerServiceDao.getServicesByUser(userId);
    }

    public void addService(CleanerServiceEntity service, UserRepository.RepositoryCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                long result = cleanerServiceDao.insertService(service);
                callback.onResult(result > 0);
            } catch (Exception e) {
                callback.onResult(false);
            }
        });
    }

    public void toggleAvailability(int serviceId, boolean available) {
        executor.execute(() -> cleanerServiceDao.updateAvailability(serviceId, available));
    }

    public void deleteService(int serviceId) {
        executor.execute(() -> cleanerServiceDao.deleteService(serviceId));
    }

    public LiveData<List<ServiceCategoryEntity>> getAllCategories() {
        return serviceCategoryDao.getAllActiveCategories();
    }
}
