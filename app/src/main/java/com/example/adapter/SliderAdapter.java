package com.example.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.middle_group2.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private ArrayList<Integer> listImageSlider;
    private ViewPager2 viewPager2;
    private ArrayList<Integer> newList;

    public SliderAdapter(ArrayList<Integer> listImageSlider, ViewPager2 viewPager2) {
        this.listImageSlider = listImageSlider;
        this.viewPager2 = viewPager2;
        this.newList = listImageSlider;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_homepage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.imageView.setImageResource(listImageSlider.get(position));
    }

    @Override
    public int getItemCount() {
        return listImageSlider.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
            System.out.println("Log view: " + itemView.findViewById(R.id.imageSlider));
        }
    }
}
