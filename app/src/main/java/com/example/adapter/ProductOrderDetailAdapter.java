package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.R;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;

public class ProductOrderDetailAdapter extends RecyclerView.Adapter<ProductOrderDetailAdapter.ProductOrderDetailViewHolder> {


    private Context context;
    private ArrayList<HashMap<String, Object>> data;

    public ProductOrderDetailAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ProductOrderDetailAdapter.ProductOrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_order_detail, parent, false);
        ProductOrderDetailAdapter.ProductOrderDetailViewHolder productViewHolder = new ProductOrderDetailAdapter.ProductOrderDetailViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderDetailAdapter.ProductOrderDetailViewHolder holder, int position) {
        HashMap<String, Object> map = data.get(position);
        Product product = (Product) map.get("product");
        String quantity = (String) map.get("quantity");
        int resId = context.getResources().getIdentifier(product.getNameFileMain(), product.getDefType(), context.getPackageName());
        holder.ivProduct.setImageResource(resId);
        holder.tvName.setText(product.getNameProduct());
        holder.tvPrice.setText(product.getCurrency() + product.getCurrentPrice());
        holder.tvQuantity.setText("x " + quantity);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ProductOrderDetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvQuantity;
        public ProductOrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivCartProduct);
            tvName = itemView.findViewById(R.id.tvNameProductCart);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
