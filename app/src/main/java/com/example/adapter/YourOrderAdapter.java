package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.R;
import com.example.utils.Constants;

import java.util.ArrayList;

import entities.PurchaseOrder;

public class YourOrderAdapter extends RecyclerView.Adapter<YourOrderAdapter.YourOrderViewHolder>{

    private ArrayList<PurchaseOrder> listPurchaseOrder;
    private Context context;
    private int[] listQuantity;

    public YourOrderAdapter(Context context, ArrayList<PurchaseOrder> listPurchaseOrder, int[] listQuantity) {
        this.context = context;
        this.listPurchaseOrder = listPurchaseOrder;
        this.listQuantity = listQuantity;
    }

    @NonNull
    @Override
    public YourOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.your_order_item, parent, false);

        return new YourOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourOrderViewHolder holder, int position) {
        PurchaseOrder item = listPurchaseOrder.get(position);
        holder.tvPONumber.setText(item.getPurchaseOrderNumber());
        holder.tvDateOrder.setText("Ordered at " + item.getOrderDate());
        String status = "";
        if(item.getStatus().equals(Constants.STATUS_ORDER)) {
            status = "Ordered successfully";
        } else if (item.getStatus().equals(Constants.STATUS_INTRANSIT)) {
            status =  "In transit";
        } else if (item.getStatus().equals(Constants.STATUS_DELIVERED)) {
            status =  "Delivered successfully";
        }
        holder.orderStatus.setText(status);
        int quantityItemOrder = listQuantity[position];
        holder.itemQuantity.setText(quantityItemOrder + (quantityItemOrder == 1 ? " Item purchased" : " Items purchased"));
        holder.totalPrice.setText("$" + item.getPayment());
    }

    @Override
    public int getItemCount() {
        return listPurchaseOrder == null ? 0 : listPurchaseOrder.size();
    }

    public class YourOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPONumber, tvDateOrder, orderStatus, itemQuantity, totalPrice;
        public YourOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPONumber = itemView.findViewById(R.id.tvPONumber);
            tvDateOrder = itemView.findViewById(R.id.tvDateOrder);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}
