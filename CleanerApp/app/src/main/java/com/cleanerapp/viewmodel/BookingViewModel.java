package com.cleanerapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanerapp.data.db.entities.BookingEntity;
import com.cleanerapp.data.repository.BookingRepository;
import com.cleanerapp.utils.SessionManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BookingViewModel extends ViewModel {

    private final BookingRepository bookingRepository;
    private final SessionManager sessionManager;

    private final MutableLiveData<Boolean> bookingResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public BookingViewModel(BookingRepository bookingRepository, SessionManager sessionManager) {
        this.bookingRepository = bookingRepository;
        this.sessionManager = sessionManager;
    }

    public LiveData<Boolean> getBookingResult() { return bookingResult; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<List<BookingEntity>> getMyBookings() {
        return bookingRepository.getBookingsByUser(sessionManager.getUserId());
    }

    public LiveData<List<BookingEntity>> getCleanerBookings() {
        return bookingRepository.getBookingsByCleaner(sessionManager.getUserId());
    }

    public void createBooking(int cleanerId, int serviceId, String cleanerName,
                               String serviceType, double price, long bookingDate, String address) {
        isLoading.setValue(true);
        int userId = sessionManager.getUserId();
        BookingEntity booking = new BookingEntity(
                userId, cleanerId, serviceId, cleanerName, serviceType, price, bookingDate, address
        );
        bookingRepository.createBooking(booking, result -> {
            isLoading.postValue(false);
            bookingResult.postValue(result);
        });
    }

    public void updateBookingStatus(int bookingId, String status) {
        bookingRepository.updateStatus(bookingId, status);
    }
}
