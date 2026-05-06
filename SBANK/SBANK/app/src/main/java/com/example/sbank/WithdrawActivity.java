package com.example.sbank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WithdrawActivity extends AppCompatActivity {

    private EditText etAccNo, etPassword, etAmount;
    private Button btnWithdraw;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        dbHelper = new DatabaseHelper(this);

        etAccNo = findViewById(R.id.etAccNo);
        etPassword = findViewById(R.id.etPassword);
        etAmount = findViewById(R.id.etAmount);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        btnWithdraw.setOnClickListener(v -> {
            String accNo = etAccNo.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (accNo.isEmpty() || password.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.verifyPassword(accNo, password)) {
                if (dbHelper.withdraw(accNo, amount)) {
                    Toast.makeText(this, "Withdrawal Successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid Account No or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
