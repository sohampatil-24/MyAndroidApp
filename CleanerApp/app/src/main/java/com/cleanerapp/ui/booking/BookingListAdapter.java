package com.cleanerapp.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanerapp.R;
import com.cleanerapp.data.db.entities.BookingEntity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingListAdapter extends ListAdapter<BookingEntity, BookingListAdapter.ViewHolder> {

    public interface OnStatusChangeListener {
        void onStatusChange(BookingEntity booking, String newStatus);
    }

    private final boolean isCleanerView;
    private final OnStatusChangeListener listener;

    public BookingListAdapter(boolean isCleanerView, OnStatusChangeListener listener) {
        super(DIFF_CALLBACK);
        this.isCleanerView = isCleanerView;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<BookingEntity> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<BookingEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull BookingEntity o, @NonNull BookingEntity n) {
                return o.id == n.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull BookingEntity o, @NonNull BookingEntity n) {
                return o.status.equals(n.status);
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), isCleanerView, listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCleanerName, tvServiceType, tvStatus, tvPrice, tvDate, tvAddress;
        MaterialButton btnConfirm, btnComplete;

        ViewHolder(View v) {
            super(v);
            tvCleanerName = v.findViewById(R.id.tvCleanerName);
            tvServiceType = v.findViewById(R.id.tvServiceType);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvDate = v.findViewById(R.id.tvDate);
            tvAddress = v.findViewById(R.id.tvAddress);
            btnConfirm = v.findViewById(R.id.btnConfirm);
            btnComplete = v.findViewById(R.id.btnComplete);
        }

        void bind(BookingEntity booking, boolean isCleanerView, OnStatusChangeListener listener) {
            tvCleanerName.setText(booking.cleanerName);
            tvServiceType.setText(booking.serviceType);
            tvStatus.setText(booking.status);
            tvPrice.setText(String.format("₹%.0f", booking.price));
            tvAddress.setText(booking.address);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            tvDate.setText(sdf.format(new Date(booking.createdAt)));

            if (isCleanerView && listener != null) {
                btnConfirm.setVisibility("PENDING".equals(booking.status) ? View.VISIBLE : View.GONE);
                btnComplete.setVisibility("CONFIRMED".equals(booking.status) ? View.VISIBLE : View.GONE);
                btnConfirm.setOnClickListener(v -> listener.onStatusChange(booking, "CONFIRMED"));
                btnComplete.setOnClickListener(v -> listener.onStatusChange(booking, "COMPLETED"));
            } else {
                btnConfirm.setVisibility(View.GONE);
                btnComplete.setVisibility(View.GONE);
            }
        }
    }
}
