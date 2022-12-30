package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.middle_group2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<HashMap<String, String>> data;
    public AddressAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String, String>> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HashMap<String, String> item = data.get(position);
        return handleView(item, convertView);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HashMap<String, String> item = data.get(position);
        return handleView(item, convertView);
    }

    private View handleView(HashMap<String, String> item, View convertView){
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView tvLabel = convertView.findViewById(R.id.tvLabel);
        tvLabel.setText(item.get("name"));
        return convertView;
    }
}
