package com.example.sbank;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;

    public TransactionAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            return cursor;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);
        }

        TextView tvAccNo = convertView.findViewById(R.id.tvAccNo);
        TextView tvType = convertView.findViewById(R.id.tvType);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvTimestamp = convertView.findViewById(R.id.tvTimestamp);

        if (cursor != null && cursor.moveToPosition(position)) {
            String accNo = cursor.getString(cursor.getColumnIndexOrThrow("account_no"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

            tvAccNo.setText("Acc: " + accNo);
            tvType.setText(type);
            tvAmount.setText("₹" + String.format("%.2f", amount));
            tvTimestamp.setText(timestamp);

            if (type.equalsIgnoreCase("Deposit")) {
                tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        return convertView;
    }
}
