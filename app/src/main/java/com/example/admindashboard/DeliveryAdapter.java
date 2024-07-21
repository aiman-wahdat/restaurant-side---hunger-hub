package com.example.admindashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {

    private List<OrderItem> deliveryOrders;

    public void setItems(List<OrderItem> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = deliveryOrders.get(position);
        holder.bind(orderItem);
    }

    @Override
    public int getItemCount() {
        return deliveryOrders != null ? deliveryOrders.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView customerNameTextView;
        private TextView paymentTextView;
        private TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerName);
            paymentTextView = itemView.findViewById(R.id.customerPayment);
            statusTextView = itemView.findViewById(R.id.statusMoney);
        }

        public void bind(OrderItem orderItem) {
            customerNameTextView.setText(orderItem.getOrderId());
            paymentTextView.setText(orderItem.getOrderPrice());
            statusTextView.setText(orderItem.getOrderStatus());
        }
    }
}

