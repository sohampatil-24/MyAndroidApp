package com.cleanerapp.ui.cleaner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanerapp.R;
import com.cleanerapp.data.db.entities.CleanerServiceEntity;
import com.google.android.material.button.MaterialButton;

public class MyServicesAdapter extends ListAdapter<CleanerServiceEntity, MyServicesAdapter.ViewHolder> {

    public interface OnToggleListener { void onToggle(CleanerServiceEntity service); }
    public interface OnDeleteListener { void onDelete(CleanerServiceEntity service); }

    private final OnToggleListener toggleListener;
    private final OnDeleteListener deleteListener;

    public MyServicesAdapter(OnToggleListener toggleListener, OnDeleteListener deleteListener) {
        super(DIFF_CALLBACK);
        this.toggleListener = toggleListener;
        this.deleteListener = deleteListener;
    }

    private static final DiffUtil.ItemCallback<CleanerServiceEntity> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<CleanerServiceEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull CleanerServiceEntity o, @NonNull CleanerServiceEntity n) {
                return o.id == n.id;
            }
            @Override
            public boolean areContentsTheSame(@NonNull CleanerServiceEntity o, @NonNull CleanerServiceEntity n) {
                return o.isAvailable == n.isAvailable && o.price == n.price;
            }
        };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), toggleListener, deleteListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvPrice, tvDesc;
        Switch switchAvailable;
        MaterialButton btnDelete;

        ViewHolder(View v) {
            super(v);
            tvType = v.findViewById(R.id.tvServiceType);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvDesc = v.findViewById(R.id.tvDescription);
            switchAvailable = v.findViewById(R.id.switchAvailable);
            btnDelete = v.findViewById(R.id.btnDelete);
        }

        void bind(CleanerServiceEntity service, OnToggleListener toggle, OnDeleteListener delete) {
            tvType.setText(service.serviceType);
            tvPrice.setText(String.format("₹%.0f", service.price));
            tvDesc.setText(service.description);
            switchAvailable.setChecked(service.isAvailable);
            switchAvailable.setOnCheckedChangeListener((btn, isChecked) -> toggle.onToggle(service));
            btnDelete.setOnClickListener(v -> delete.onDelete(service));
        }
    }
}
