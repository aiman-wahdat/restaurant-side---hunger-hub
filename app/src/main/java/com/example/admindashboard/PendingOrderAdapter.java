package com.example.admindashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {

    private List<OrderItem> pendingOrders;
    private OnOrderActionListener listener;

    public void setItems(List<OrderItem> pendingOrders) {
        this.pendingOrders = pendingOrders;
        notifyDataSetChanged();
    }

    public void setOnOrderActionListener(OnOrderActionListener listener) {
        this.listener = listener;
    }
    public void removeItem(int position) {
        if (pendingOrders != null && position >= 0 && position < pendingOrders.size()) {
            pendingOrders.remove(position);
            notifyItemRemoved(position); // Notify adapter about item removal
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = pendingOrders.get(position);
        holder.bind(orderItem);
    }

    @Override
    public int getItemCount() {
        return pendingOrders != null ? pendingOrders.size() : 0;
    }

    public List<OrderItem> getPendingOrders() {
        return pendingOrders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView customerNameTextView;
        private TextView quantityTextView;
        private TextView paymentTextView;
        private TextView statusTextView;
        private Button acceptButton;
        private Button rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerName);
            quantityTextView = itemView.findViewById(R.id.pendingOrderQuantity);
            acceptButton = itemView.findViewById(R.id.orderacceptButton);
            rejectButton = itemView.findViewById(R.id.orderRejectButton);
            paymentTextView = itemView.findViewById(R.id.customerPayment);
            statusTextView = itemView.findViewById(R.id.status);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAccept(pendingOrders.get(position));
                    }
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onReject(pendingOrders.get(position));
                    }
                }
            });
        }

        public void bind(OrderItem orderItem) {
            customerNameTextView.setText(orderItem.getOrderId());
            paymentTextView.setText(String.valueOf(orderItem.getOrderPrice()));
            statusTextView.setText(String.valueOf(orderItem.getOrderStatus()));
        }
    }

    public interface OnOrderActionListener {
        void onAccept(OrderItem orderItem);
        void onReject(OrderItem orderItem);
    }
}
