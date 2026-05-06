package com.example.sbank;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionsActivity extends AppCompatActivity {

    private ListView listViewTransactions;
    private DatabaseHelper dbHelper;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        listViewTransactions = findViewById(R.id.listViewTransactions);
        dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getAllTransactions();
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new TransactionAdapter(this, cursor);
            listViewTransactions.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show();
        }
    }
}
