package com.cleanerapp.ui.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cleanerapp.R;
import com.cleanerapp.databinding.ActivityUserHomeBinding;
import com.cleanerapp.ui.auth.LoginActivity;
import com.cleanerapp.ui.booking.BookingActivity;
import com.cleanerapp.utils.LocationHelper;
import com.cleanerapp.utils.SessionManager;
import com.cleanerapp.viewmodel.CleanerViewModel;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserHomeActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private ActivityUserHomeBinding binding;
    private CleanerViewModel cleanerViewModel;
    private CleanerListAdapter cleanerAdapter;
    private LocationHelper locationHelper;
    private double userLat = 0, userLng = 0;

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        cleanerViewModel = new ViewModelProvider(this).get(CleanerViewModel.class);
        locationHelper = new LocationHelper(this);

        setupDrawer();
        setupRecyclerView();
        setupCategoryChips();
        setupSearch();
        setupObservers();
        requestLocationPermission();

        binding.tvWelcome.setText("Hello, " + sessionManager.getUserName() + " 👋");
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_bookings) {
                startActivity(new Intent(this, BookingActivity.class));
            } else if (id == R.id.nav_logout) {
                sessionManager.clearSession();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            binding.drawerLayout.closeDrawers();
            return true;
        });
    }

    private void setupRecyclerView() {
        cleanerAdapter = new CleanerListAdapter(service -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("service_id", service.id);
            intent.putExtra("cleaner_id", service.userId);
            intent.putExtra("cleaner_name", service.cleanerName);
            intent.putExtra("service_type", service.serviceType);
            intent.putExtra("price", service.price);
            startActivity(intent);
        });
        binding.rvCleaners.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCleaners.setAdapter(cleanerAdapter);
    }

    private void setupCategoryChips() {
        cleanerViewModel.getCategories().observe(this, categories -> {
            binding.chipGroupCategories.removeAllViews();

            // Add "All" chip
            com.google.android.material.chip.Chip allChip = new com.google.android.material.chip.Chip(this);
            allChip.setText("All");
            allChip.setCheckable(true);
            allChip.setChecked(true);
            allChip.setOnClickListener(v -> observeAllServices());
            binding.chipGroupCategories.addView(allChip);

            for (var category : categories) {
                com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(this);
                chip.setText(category.name);
                chip.setCheckable(true);
                chip.setOnClickListener(v -> filterByCategory(category.name));
                binding.chipGroupCategories.addView(chip);
            }
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    observeAllServices();
                } else {
                    cleanerViewModel.searchServices(query).observe(UserHomeActivity.this, services -> {
                        cleanerAdapter.submitList(services);
                        updateEmptyState(services == null || services.isEmpty());
                    });
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupObservers() {
        observeAllServices();
    }

    private void observeAllServices() {
        cleanerViewModel.getAllAvailableServices().observe(this, services -> {
            cleanerAdapter.submitList(services);
            updateEmptyState(services == null || services.isEmpty());
            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.setVisibility(View.GONE);
            binding.rvCleaners.setVisibility(View.VISIBLE);
        });
    }

    private void filterByCategory(String category) {
        cleanerViewModel.getServicesByType(category).observe(this, services -> {
            cleanerAdapter.submitList(services);
            updateEmptyState(services == null || services.isEmpty());
        });
    }

    private void updateEmptyState(boolean isEmpty) {
        binding.layoutEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.rvCleaners.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            fetchLocation();
        }
    }

    private void fetchLocation() {
        locationHelper.getCurrentLocation(new LocationHelper.LocationCallback() {
            @Override
            public void onLocationReceived(double latitude, double longitude) {
                userLat = latitude;
                userLng = longitude;
                runOnUiThread(() -> binding.tvLocation.setText(
                        String.format("📍 %.4f, %.4f", latitude, longitude)));
            }
            @Override
            public void onLocationFailed(String error) {
                runOnUiThread(() -> binding.tvLocation.setText("📍 Location unavailable"));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                binding.tvLocation.setText("📍 Enable location for nearby cleaners");
            }
        }
    }
}
