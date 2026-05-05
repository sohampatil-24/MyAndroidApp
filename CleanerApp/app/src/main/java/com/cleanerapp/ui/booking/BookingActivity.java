package com.cleanerapp.ui.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cleanerapp.databinding.ActivityBookingBinding;
import com.cleanerapp.utils.SessionManager;
import com.cleanerapp.viewmodel.BookingViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookingActivity extends AppCompatActivity {

    private ActivityBookingBinding binding;
    private BookingViewModel bookingViewModel;

    @Inject
    SessionManager sessionManager;

    private int serviceId, cleanerId;
    private String cleanerName, serviceType;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        // Get intent extras
        serviceId = getIntent().getIntExtra("service_id", -1);
        cleanerId = getIntent().getIntExtra("cleaner_id", -1);
        cleanerName = getIntent().getStringExtra("cleaner_name");
        serviceType = getIntent().getStringExtra("service_type");
        price = getIntent().getDoubleExtra("price", 0);

        if (serviceId == -1) {
            // Show bookings list
            setupBookingsList();
        } else {
            // Show booking form
            setupBookingForm();
        }

        setupObservers();
    }

    private void setupBookingForm() {
        binding.layoutBookingForm.setVisibility(View.VISIBLE);
        binding.layoutBookingList.setVisibility(View.GONE);

        binding.tvCleanerName.setText(cleanerName);
        binding.tvServiceType.setText(serviceType);
        binding.tvPrice.setText(String.format("₹%.0f", price));

        binding.btnConfirmBooking.setOnClickListener(v -> {
            String address = binding.etAddress.getText().toString().trim();
            if (address.isEmpty()) {
                binding.etAddress.setError("Enter your address");
                return;
            }
            bookingViewModel.createBooking(
                    cleanerId, serviceId, cleanerName, serviceType, price,
                    System.currentTimeMillis(), address
            );
        });
    }

    private void setupBookingsList() {
        binding.layoutBookingForm.setVisibility(View.GONE);
        binding.layoutBookingList.setVisibility(View.VISIBLE);

        BookingListAdapter adapter = new BookingListAdapter(false, null);
        binding.rvBookings.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBookings.setAdapter(adapter);

        bookingViewModel.getMyBookings().observe(this, bookings -> {
            adapter.submitList(bookings);
            binding.tvNoBookings.setVisibility(
                    (bookings == null || bookings.isEmpty()) ? View.VISIBLE : View.GONE);
        });
    }

    private void setupObservers() {
        bookingViewModel.getIsLoading().observe(this, loading -> {
            if (binding.btnConfirmBooking != null) {
                binding.btnConfirmBooking.setEnabled(!loading);
            }
        });

        bookingViewModel.getBookingResult().observe(this, success -> {
            if (success != null) {
                if (success) {
                    Toast.makeText(this, "Booking confirmed! Cleaner will be notified.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
