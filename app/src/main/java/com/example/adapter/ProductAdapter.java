package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.DetailProductActivity;
import com.example.middle_group2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import entities.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context context;
    private ArrayList<Product> products = new ArrayList<>();
    private Intent intentDetailProduct;

    public void setData(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public ProductAdapter(Context context) {
        this.context = context;
        this.intentDetailProduct = new Intent(context, DetailProductActivity.class);
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashsale_product, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvNameProduct.setText(product.getNameProduct());
        holder.tvCurrentPrice.setText(String.valueOf(product.getCurrency() + product.getCurrentPrice()));
        holder.tvOldPrice.setText(product.getCurrency() + String.valueOf(product.getOldPrice()));
        holder.tvSale.setText(String.valueOf(product.getSale()) + "% Off");
        int resId = context.getResources().getIdentifier(product.getNameFileMain(), product.getDefType(), context.getPackageName());
        holder.flashSaleImg.setImageResource(resId);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickToMoveDetailPage(context, product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    private void onClickToMoveDetailPage(Context context, Product product){
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        this.intentDetailProduct.putExtras(bundle);
        context.startActivity(intentDetailProduct);
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layoutProduct;
        private TextView tvNameProduct;
        private TextView tvCurrentPrice;
        private TextView tvOldPrice;
        private TextView tvSale;
        private ImageButton imageButton;
        private ImageView flashSaleImg;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvSale = itemView.findViewById(R.id.tvSale);
            layoutProduct = itemView.findViewById(R.id.layout_product);
            imageButton = itemView.findViewById(R.id.imgProduct);
            flashSaleImg = itemView.findViewById(R.id.imvFSProduct);
        }
    }



}
