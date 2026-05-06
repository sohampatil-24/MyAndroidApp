package com.example.sbank;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddAccountActivity extends AppCompatActivity {

    private EditText etAccNo, etName, etMobile, etBalance, etPassword;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        dbHelper = new DatabaseHelper(this);

        etAccNo = findViewById(R.id.etAccNo);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etBalance = findViewById(R.id.etBalance);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String accNo = etAccNo.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String balanceStr = etBalance.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (accNo.isEmpty() || name.isEmpty() || mobile.isEmpty() || balanceStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double balance;
            try {
                balance = Double.parseDouble(balanceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid balance amount", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = dbHelper.addAccount(accNo, name, mobile, balance, password);
            if (isInserted) {
                Toast.makeText(this, "Account Added Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed or Account already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
