package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.middle_group2.R;
import com.example.utils.Constants;

import java.util.ArrayList;

import entities.Navigation;


public class NavigationAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Navigation> navigations;
    private Constants constants = new Constants();
    private int preIndex = 0;
    public NavigationAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Navigation> navigations) {
        super(context, resource, navigations);
        this.context = context;
        this.resource = resource;
        this.navigations = navigations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageButton icon = convertView.findViewById(R.id.btnNavigate);
        TextView title = convertView.findViewById(R.id.titleNavigate);
        CardView cardView = convertView.findViewById(R.id.cardView);
        Navigation item = navigations.get(position);
        icon.setImageResource(item.getIcon());
        icon.setColorFilter(Color.parseColor("#9098B1"));
        title.setText(item.getTitle());
        if(item.isActive()){
            icon.setColorFilter(Color.parseColor(constants.colorActive));
            title.setTextColor(Color.parseColor(constants.colorActive));
        }
        if(!item.getTitle().equals("Cart")){
            cardView.setVisibility(View.INVISIBLE);
        }
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNavigation(position);
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNavigation(position);
            }
        });
        return convertView;
    }

    private void onClickNavigation(int position) {
        if(position != preIndex){
            navigations.get(position).setActive(true);
            navigations.get(preIndex).setActive(false);
            preIndex = position;
            notifyDataSetChanged();
        }
    }
}
