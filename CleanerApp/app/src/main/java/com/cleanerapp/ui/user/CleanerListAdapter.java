package com.cleanerapp.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanerapp.R;
import com.cleanerapp.data.db.entities.CleanerServiceEntity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class CleanerListAdapter extends ListAdapter<CleanerServiceEntity, CleanerListAdapter.ViewHolder> {

    public interface OnBookClickListener {
        void onBook(CleanerServiceEntity service);
    }

    private final OnBookClickListener listener;

    public CleanerListAdapter(OnBookClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<CleanerServiceEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CleanerServiceEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull CleanerServiceEntity oldItem,
                                                @NonNull CleanerServiceEntity newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull CleanerServiceEntity oldItem,
                                                   @NonNull CleanerServiceEntity newItem) {
                    return oldItem.isAvailable == newItem.isAvailable &&
                           oldItem.price == newItem.price;
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cleaner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CleanerServiceEntity service = getItem(position);
        holder.bind(service, listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvServiceType, tvPrice, tvRating, tvDescription;
        private final MaterialButton btnBook;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCleanerName);
            tvServiceType = itemView.findViewById(R.id.tvServiceType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnBook = itemView.findViewById(R.id.btnBook);
        }

        void bind(CleanerServiceEntity service, OnBookClickListener listener) {
            tvName.setText(service.cleanerName != null ? service.cleanerName : "Cleaner");
            tvServiceType.setText(service.serviceType);
            tvPrice.setText(String.format("₹%.0f", service.price));
            tvRating.setText(String.format("⭐ %.1f", service.rating));
            tvDescription.setText(service.description != null ? service.description : "Professional cleaning service");
            btnBook.setOnClickListener(v -> listener.onBook(service));
        }
    }
}
