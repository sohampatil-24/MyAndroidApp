package com.example.sbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button btnAddAccount = findViewById(R.id.btnAddAccount);
        Button btnDeposit = findViewById(R.id.btnDeposit);
        Button btnWithdraw = findViewById(R.id.btnWithdraw);
        Button btnViewAccount = findViewById(R.id.btnViewAccount);
        Button btnTransactions = findViewById(R.id.btnTransactions);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAddAccount.setOnClickListener(v -> startActivity(new Intent(this, AddAccountActivity.class)));
        btnDeposit.setOnClickListener(v -> startActivity(new Intent(this, DepositActivity.class)));
        btnWithdraw.setOnClickListener(v -> startActivity(new Intent(this, WithdrawActivity.class)));
        btnViewAccount.setOnClickListener(v -> startActivity(new Intent(this, ViewAccountActivity.class)));
        btnTransactions.setOnClickListener(v -> startActivity(new Intent(this, TransactionsActivity.class)));
        
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
