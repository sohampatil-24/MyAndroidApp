package com.cleanerapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cleanerapp.databinding.ActivityRegisterBinding;
import com.cleanerapp.ui.cleaner.CleanerDashboardActivity;
import com.cleanerapp.ui.user.UserHomeActivity;
import com.cleanerapp.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;
    private String selectedRole = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupRoleToggle();
        setupObservers();
        setupClickListeners();
    }

    private void setupRoleToggle() {
        binding.toggleRole.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == com.cleanerapp.R.id.btnUser) {
                    selectedRole = "USER";
                } else if (checkedId == com.cleanerapp.R.id.btnCleaner) {
                    selectedRole = "CLEANER";
                }
            }
        });
        binding.toggleRole.check(com.cleanerapp.R.id.btnUser);
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnRegister.setEnabled(!isLoading);
        });

        authViewModel.getAuthResult().observe(this, result -> {
            if (result.success) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                navigateBasedOnRole(selectedRole);
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            authViewModel.register(name, email, phone, password, selectedRole);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void navigateBasedOnRole(String role) {
        Intent intent;
        if ("CLEANER".equals(role)) {
            intent = new Intent(this, CleanerDashboardActivity.class);
        } else {
            intent = new Intent(this, UserHomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
