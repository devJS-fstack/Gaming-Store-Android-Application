package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.adapter.AddressItemAdapter;
import com.example.implement.AddressImp;

import java.util.ArrayList;

import entities.Address;

public class AddressActivity extends AppCompatActivity {

    // component
    private RecyclerView rvListAddress;
    private TextView tvHeadingAddress;
    public static Button btnNextCart;

    // data
    private AddressImp addressImp = new AddressImp();
    private ArrayList<Address> listAddress;
    private AddressItemAdapter addressItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address);
        setControl();
        initial();
        if (!AddressItemAdapter.isOrder) {
            btnNextCart.setVisibility(View.VISIBLE);
            btnNextCart.setText("Add");
            tvHeadingAddress.setText("My Addresses");
        }

        btnNextCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                AddressActivity.this.startActivity(intent);
            }
        });
    }

    private void initial() {
        listAddress = addressImp.findAll(LoginActivity.idUserLogin);
        addressItemAdapter = new AddressItemAdapter(this, listAddress, tvHeadingAddress);
        rvListAddress.setAdapter(addressItemAdapter);
        rvListAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setControl() {
        rvListAddress = findViewById(R.id.rvListAddress);
        tvHeadingAddress = findViewById(R.id.tvHeadingAddress);
        btnNextCart = findViewById(R.id.btnNextCart);
    }
}