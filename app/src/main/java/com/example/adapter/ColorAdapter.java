package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    Context context;
    ArrayList<Integer> data;
    int positionSelect = -1;
    public static int colorSelect;
    public ColorAdapter(@NonNull Context context, @NonNull ArrayList<Integer> data){
        this.context=context;
        this.data=data;
    }
    
    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_product, parent, false);
        ColorViewHolder colorViewHolder = new ColorViewHolder(view);
        return colorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int item = data.get(position);
        if(position == positionSelect) {
            holder.cvColorSelect.setVisibility(View.VISIBLE);
            colorSelect = item;
        } else {
            holder.cvColorSelect.setVisibility(View.INVISIBLE);
        }
        holder.selectColor.setImageResource(item);
        holder.selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionSelect = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private ImageButton selectColor;
        private CardView cvColorSelect;
        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            selectColor = itemView.findViewById(R.id.btnSelectColor);
            cvColorSelect = itemView.findViewById(R.id.cvColorSelect);
        }
    }

}
