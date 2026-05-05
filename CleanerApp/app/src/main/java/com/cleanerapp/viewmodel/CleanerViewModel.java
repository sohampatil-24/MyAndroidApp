package com.cleanerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanerapp.data.db.entities.CleanerServiceEntity;
import com.cleanerapp.data.db.entities.ServiceCategoryEntity;
import com.cleanerapp.data.repository.CleanerRepository;
import com.cleanerapp.data.repository.UserRepository;
import com.cleanerapp.utils.SessionManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CleanerViewModel extends ViewModel {

    private final CleanerRepository cleanerRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> serviceAddResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public CleanerViewModel(CleanerRepository cleanerRepository, SessionManager sessionManager) {
        this.cleanerRepository = cleanerRepository;
        this.sessionManager = sessionManager;
    }

    public LiveData<List<CleanerServiceEntity>> getAllAvailableServices() {
        return cleanerRepository.getAllAvailableServices();
    }

    public LiveData<List<CleanerServiceEntity>> searchServices(String query) {
        return cleanerRepository.searchServices(query);
    }

    public LiveData<List<CleanerServiceEntity>> getServicesByType(String type) {
        return cleanerRepository.getServicesByType(type);
    }

    public LiveData<List<CleanerServiceEntity>> getMyServices() {
        int userId = sessionManager.getUserId();
        return cleanerRepository.getServicesByUser(userId);
    }

    public LiveData<List<ServiceCategoryEntity>> getCategories() {
        return cleanerRepository.getAllCategories();
    }

    public LiveData<Boolean> getServiceAddResult() { return serviceAddResult; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void addService(String serviceType, String description, double price,
                           double latitude, double longitude) {
        isLoading.setValue(true);
        int userId = sessionManager.getUserId();
        String name = sessionManager.getUserName();
        // phone is not stored in SessionManager by default, fetch from DB or use placeholder
        CleanerServiceEntity service = new CleanerServiceEntity(
                userId, name, serviceType, description, price, latitude, longitude, ""
        );
        cleanerRepository.addService(service, result -> {
            isLoading.postValue(false);
            serviceAddResult.postValue(result);
        });
    }

    public void toggleAvailability(int serviceId, boolean available) {
        cleanerRepository.toggleAvailability(serviceId, available);
    }

    public void deleteService(int serviceId) {
        cleanerRepository.deleteService(serviceId);
    }
}
