package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.middle_group2.AddAddressActivity;
import com.example.middle_group2.AddressActivity;
import com.example.middle_group2.OrderDetailActivity;
import com.example.middle_group2.R;

import java.util.ArrayList;

import entities.Address;

public class AddressItemAdapter extends RecyclerView.Adapter<AddressItemAdapter.AddressItemViewHolder> {
    private Context context;
    private ArrayList<Address> listAddress;
    public static Boolean isOrder = false;
    private RadioButton lastChecked;
    private TextView tvHeadingAddress;
    public static Address addressSelected;

    public AddressItemAdapter(Context context, ArrayList<Address> listAddress, TextView tvHeadingAddress) {
        this.context = context;
        this.listAddress = listAddress;
        this.tvHeadingAddress = tvHeadingAddress;
    }

    @NonNull
    @Override
    public AddressItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_by_user, parent, false);
        return new AddressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressItemViewHolder holder, int position) {
        int index = position;
        Address item = listAddress.get(position);
        holder.tvFullName.setText(item.getFullName());
        holder.tvCity.setText(item.getWard() + ", " + item.getDistrict() + ", " + item.getCity());
        holder.tvPhone.setText(item.getPhone());
        holder.tvStreet.setText(item.getStreet());
        if(!isOrder) {
            holder.radioBtnAddress.setVisibility(View.INVISIBLE);
            AddressActivity.btnNextCart.setVisibility(View.VISIBLE);
            AddressActivity.btnNextCart.setText("Add");
            tvHeadingAddress.setText("My Addresses");
        } else {
            holder.radioBtnAddress.setVisibility(View.VISIBLE);
            tvHeadingAddress.setText("Address Selection");
            AddressActivity.btnNextCart.setText("Next");
            AddressActivity.btnNextCart.setVisibility(View.VISIBLE);
            if(item.getDefault()) {
                holder.radioBtnAddress.setChecked(true);
                lastChecked = holder.radioBtnAddress;
                addressSelected = item;
            }

            holder.radioBtnAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lastChecked != holder.radioBtnAddress) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                        }
                        lastChecked = holder.radioBtnAddress;
                        addressSelected = item;
                    }
                }
            });
        }

        AddressActivity.btnNextCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOrder) {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAddress == null ? 0 : listAddress.size();
    }

    public class AddressItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFullName, tvPhone, tvStreet, tvCity, tvHeadingAddress;
        private RadioButton radioBtnAddress;
        public AddressItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStreet = itemView.findViewById(R.id.tvStreet);
            tvCity = itemView.findViewById(R.id.tvCity);
            radioBtnAddress = itemView.findViewById(R.id.radioBtnAddress);
        }
    }
}
