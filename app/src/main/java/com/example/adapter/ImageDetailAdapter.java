package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.middle_group2.R;

import java.util.ArrayList;

import entities.ImageDetailCategory;
import entities.ImageDetailProduct;

public class ImageDetailAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageDetailProduct> listImgProduct;
    private ArrayList<ImageDetailCategory> listImgCategory;

    public ImageDetailAdapter(Context context, @Nullable ArrayList<ImageDetailProduct> listImgProduct, @Nullable ArrayList<ImageDetailCategory> listImgCategory){
        this.context = context;
        this.listImgProduct = listImgProduct;
        this.listImgCategory = listImgCategory;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_product_detail, container, false);
        ImageView img = view.findViewById(R.id.imageDetailProduct);
        if(listImgProduct != null) {
            ImageDetailProduct item = listImgProduct.get(position);
            if(item != null) {
                int resId = context.getResources().getIdentifier(item.getNameFile(), item.getDefType(), context.getPackageName());
                Glide.with(context).load(resId).into(img);
            }
        } else if (listImgCategory != null) {
            ImageDetailCategory item = listImgCategory.get(position);
            if(item != null) {
                int resId = context.getResources().getIdentifier(item.getNameFile(), item.getDefType(), context.getPackageName());
                Glide.with(context).load(resId).into(img);
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (listImgProduct != null) {
            size = listImgProduct.size();
        } else if (listImgCategory != null) {
            size = listImgCategory.size();
        }
        return size;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
