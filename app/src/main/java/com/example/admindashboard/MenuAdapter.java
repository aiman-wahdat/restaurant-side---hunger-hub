package com.example.admindashboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(String itemId);
    }

    private static OnDeleteItemClickListener deleteItemClickListener;

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        this.deleteItemClickListener = listener;
    }

    private List<MenuItem> menuItems;

    public void setItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.bind(menuItem);
    }
    public interface OnItemLongPressListener {
        void onItemLongPressed(MenuItem menuItem);
    }

    private OnItemLongPressListener longPressListener;

    // Setter method for long press listener
    public void setOnItemLongPressListener(OnItemLongPressListener listener) {
        this.longPressListener = listener;
    }
    @Override
    public int getItemCount() {
        return menuItems != null ? menuItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImageView;
        private TextView foodNameTextView;
        private TextView foodPriceTextView;

        private TextView foodDescriptionTextView;
        private ImageButton plusButton;
        private TextView foodQuantity;
        private ImageButton minusButton;
        private ImageView deleteButton;

        private Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context; // Initialize the context
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.fooditemPrice);

            foodDescriptionTextView = itemView.findViewById(R.id.foodDescriptionTextView);


            // Long press listener
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longPressListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            MenuItem menuItem = menuItems.get(position);
                            longPressListener.onItemLongPressed(menuItem);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }


        public void bind(MenuItem menuItem) {
            // Set data to views
            if (foodNameTextView == null || foodPriceTextView == null) {
                return; // Exit the method if any TextView is null
            }

            // Check for null context and image view
            if (context != null && foodImageView != null) {
                menuItem.getImageDrawable(context, new MenuItem.OnDrawableLoadedListener() {
                    @Override
                    public void onDrawableLoaded(Drawable drawable) {
                        // Check if drawable is not null before setting it to the ImageView
                        if (drawable != null) {
                            foodImageView.setImageDrawable(drawable);
                        } else {
                            // Set a default image or hide the ImageView
                            foodImageView.setImageResource(R.drawable.defaultimage);
                            // foodImageView.setVisibility(View.GONE); // Uncomment this line to hide the ImageView
                        }
                    }
                });
            }

            String priceWithCurrency = "RS. " + menuItem.getPrice();
            foodPriceTextView.setText(priceWithCurrency);
            foodNameTextView.setText(menuItem.getName());
            foodDescriptionTextView.setText(menuItem.getDescription());
            // You can bind other views similarly
        }
    }
}

