package com.example.sbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartBank.db";
    private static final int DATABASE_VERSION = 1;

    // Accounts Table
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String COL_ACC_NO = "account_no";
    private static final String COL_NAME = "name";
    private static final String COL_MOBILE = "mobile";
    private static final String COL_BALANCE = "balance";
    private static final String COL_PASSWORD = "password";

    // Transactions Table
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COL_TRANS_ID = "id";
    private static final String COL_TRANS_ACC_NO = "account_no";
    private static final String COL_TRANS_TYPE = "type";
    private static final String COL_TRANS_AMOUNT = "amount";
    private static final String COL_TRANS_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccountsTable = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                COL_ACC_NO + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT, " +
                COL_MOBILE + " TEXT, " +
                COL_BALANCE + " REAL, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createAccountsTable);

        String createTransactionsTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COL_TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRANS_ACC_NO + " TEXT, " +
                COL_TRANS_TYPE + " TEXT, " +
                COL_TRANS_AMOUNT + " REAL, " +
                COL_TRANS_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTransactionsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public boolean addAccount(String accNo, String name, String mobile, double balance, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ACC_NO, accNo);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MOBILE, mobile);
        contentValues.put(COL_BALANCE, balance);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_ACCOUNTS, null, contentValues);
        return result != -1;
    }

    public Cursor getAccount(String accNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COL_ACC_NO + " = ?", new String[]{accNo});
    }

    public boolean verifyPassword(String accNo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COL_ACC_NO + " = ? AND " + COL_PASSWORD + " = ?", new String[]{accNo, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean deposit(String accNo, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getAccount(accNo);
        if (cursor.moveToFirst()) {
            double currentBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BALANCE));
            double newBalance = currentBalance + amount;
            
            ContentValues values = new ContentValues();
            values.put(COL_BALANCE, newBalance);
            
            int rowsAffected = db.update(TABLE_ACCOUNTS, values, COL_ACC_NO + " = ?", new String[]{accNo});
            if (rowsAffected > 0) {
                logTransaction(accNo, "Deposit", amount);
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean withdraw(String accNo, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = getAccount(accNo);
        if (cursor.moveToFirst()) {
            double currentBalance = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BALANCE));
            if (currentBalance >= amount) {
                double newBalance = currentBalance - amount;
                
                ContentValues values = new ContentValues();
                values.put(COL_BALANCE, newBalance);
                
                int rowsAffected = db.update(TABLE_ACCOUNTS, values, COL_ACC_NO + " = ?", new String[]{accNo});
                if (rowsAffected > 0) {
                    logTransaction(accNo, "Withdraw", amount);
                    cursor.close();
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }

    private void logTransaction(String accNo, String type, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TRANS_ACC_NO, accNo);
        values.put(COL_TRANS_TYPE, type);
        values.put(COL_TRANS_AMOUNT, amount);
        db.insert(TABLE_TRANSACTIONS, null, values);
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COL_TRANS_TIMESTAMP + " DESC", null);
    }
}
