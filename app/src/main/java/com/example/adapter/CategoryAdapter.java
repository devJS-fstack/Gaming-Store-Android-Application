package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.ProductByCategory;
import com.example.middle_group2.R;

import java.util.ArrayList;

import entities.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {
    private Context context;
    private ArrayList<Category> categories;
    private LinearLayoutManager linearLayoutManager;
    private AssetManager assetManager;
    private Intent intentCategory;
    public CategoryAdapter(Context context, ArrayList<Category> categories, LinearLayoutManager linearLayoutManager) {
        this.context = context;
        this.categories = categories;
        this.linearLayoutManager = linearLayoutManager;
        assetManager = context.getAssets();
        this.intentCategory = new Intent(context, ProductByCategory.class);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.title.setText(category.getNameCategory());
        int resID = context.getResources().getIdentifier(category.getNameFile(), category.getDefType(), context.getPackageName());
        holder.icon.setImageResource(resID);
        holder.imgBtnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCategory(category.getIdCategory(), category.getNameCategory());
            }
        });
    }

    private void onClickCategory(String idCategory, String nameCategory) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("idCategory", idCategory);
        bundle.putSerializable("nameCategory", nameCategory);
        this.intentCategory.putExtras(bundle);
        context.startActivity(intentCategory);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;
        private ImageButton imgBtnCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconCategory);
            title = itemView.findViewById(R.id.titleCategory);
            imgBtnCategory = itemView.findViewById(R.id.imgBtnCategory);
        }
    }
}
