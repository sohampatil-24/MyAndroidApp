package com.example.sbank;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewAccountActivity extends AppCompatActivity {

    private EditText etAccNo;
    private Button btnSearch;
    private LinearLayout layoutDetails;
    private TextView tvName, tvMobile, tvBalance;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        dbHelper = new DatabaseHelper(this);

        etAccNo = findViewById(R.id.etAccNo);
        btnSearch = findViewById(R.id.btnSearch);
        layoutDetails = findViewById(R.id.layoutDetails);
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvBalance = findViewById(R.id.tvBalance);

        btnSearch.setOnClickListener(v -> {
            String accNo = etAccNo.getText().toString().trim();
            if (accNo.isEmpty()) {
                Toast.makeText(this, "Please enter Account Number", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = dbHelper.getAccount(accNo);
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
                double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

                tvName.setText("Name: " + name);
                tvMobile.setText("Mobile: " + mobile);
                tvBalance.setText("Balance: ₹" + String.format("%.2f", balance));

                layoutDetails.setVisibility(View.VISIBLE);
                cursor.close();
            } else {
                layoutDetails.setVisibility(View.GONE);
                Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
