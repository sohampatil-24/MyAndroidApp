package com.cleanerapp.ui.cleaner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cleanerapp.R;
import com.cleanerapp.databinding.ActivityCleanerDashboardBinding;
import com.cleanerapp.ui.auth.LoginActivity;
import com.cleanerapp.ui.booking.BookingListAdapter;
import com.cleanerapp.utils.LocationHelper;
import com.cleanerapp.utils.SessionManager;
import com.cleanerapp.viewmodel.BookingViewModel;
import com.cleanerapp.viewmodel.CleanerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CleanerDashboardActivity extends AppCompatActivity {

    private ActivityCleanerDashboardBinding binding;
    private CleanerViewModel cleanerViewModel;
    private BookingViewModel bookingViewModel;
    private LocationHelper locationHelper;
    private double currentLat = 0, currentLng = 0;
    private List<String> categoryList = new ArrayList<>();

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCleanerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        cleanerViewModel = new ViewModelProvider(this).get(CleanerViewModel.class);
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        locationHelper = new LocationHelper(this);

        binding.tvWelcome.setText("Welcome, " + sessionManager.getUserName());
        setupServiceTypeSpinner();
        setupMyServices();
        setupBookings();
        setupAddService();
        setupLogout();
        fetchLocation();
    }

    private void setupServiceTypeSpinner() {
        cleanerViewModel.getCategories().observe(this, categories -> {
            categoryList.clear();
            for (var cat : categories) categoryList.add(cat.name);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerServiceType.setAdapter(adapter);
        });
    }

    private void setupMyServices() {
        MyServicesAdapter adapter = new MyServicesAdapter(
                service -> cleanerViewModel.toggleAvailability(service.id, !service.isAvailable),
                service -> cleanerViewModel.deleteService(service.id)
        );
        binding.rvMyServices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMyServices.setAdapter(adapter);

        cleanerViewModel.getMyServices().observe(this, services -> {
            adapter.submitList(services);
            binding.tvNoServices.setVisibility(
                    (services == null || services.isEmpty()) ? View.VISIBLE : View.GONE);
        });
    }

    private void setupBookings() {
        BookingListAdapter bookingAdapter = new BookingListAdapter(true, (booking, status) ->
                bookingViewModel.updateBookingStatus(booking.id, status));
        binding.rvBookings.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBookings.setAdapter(bookingAdapter);

        bookingViewModel.getCleanerBookings().observe(this, bookings -> {
            bookingAdapter.submitList(bookings);
            int count = bookings != null ? bookings.size() : 0;
            binding.tvBookingCount.setText(count + " booking(s) received");
        });
    }

    private void setupAddService() {
        cleanerViewModel.getServiceAddResult().observe(this, success -> {
            if (success != null) {
                if (success) {
                    Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show();
                    binding.etDescription.setText("");
                    binding.etPrice.setText("");
                } else {
                    Toast.makeText(this, "Failed to add service", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cleanerViewModel.getIsLoading().observe(this, loading ->
                binding.btnAddService.setEnabled(!loading));

        binding.btnAddService.setOnClickListener(v -> {
            String description = binding.etDescription.getText().toString().trim();
            String priceStr = binding.etPrice.getText().toString().trim();

            if (categoryList.isEmpty()) {
                Toast.makeText(this, "Loading categories...", Toast.LENGTH_SHORT).show();
                return;
            }
            String selectedType = (String) binding.spinnerServiceType.getSelectedItem();
            if (selectedType == null || selectedType.isEmpty()) {
                Toast.makeText(this, "Select a service type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.isEmpty()) {
                binding.etDescription.setError("Add a description");
                return;
            }
            if (priceStr.isEmpty()) {
                binding.etPrice.setError("Enter price");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                binding.etPrice.setError("Invalid price");
                return;
            }

            cleanerViewModel.addService(selectedType, description, price, currentLat, currentLng);
        });
    }

    private void setupLogout() {
        binding.btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void fetchLocation() {
        locationHelper.getCurrentLocation(new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                currentLat = latitude;
                currentLng = longitude;
                runOnUiThread(() -> binding.tvLocation.setText(
                        String.format("📍 %.4f, %.4f", latitude, longitude)));
            }
            @Override
            public void onLocationFailed(String error) {
                runOnUiThread(() -> binding.tvLocation.setText("📍 Enable GPS for location"));
            }
        });
    }
}
