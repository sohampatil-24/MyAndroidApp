package com.cleanerapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cleanerapp.databinding.ActivityLoginBinding;
import com.cleanerapp.ui.cleaner.CleanerDashboardActivity;
import com.cleanerapp.ui.user.UserHomeActivity;
import com.cleanerapp.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupObservers();
        setupClickListeners();
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnLogin.setEnabled(!isLoading);
        });

        authViewModel.getAuthResult().observe(this, result -> {
            if (result.success) {
                navigateBasedOnRole(authViewModel.getUserRole());
            } else {
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            authViewModel.login(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
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
