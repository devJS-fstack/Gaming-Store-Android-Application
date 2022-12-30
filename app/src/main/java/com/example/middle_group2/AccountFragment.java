package com.example.middle_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adapter.AddressItemAdapter;

public class AccountFragment extends Fragment {

    // component
    LinearLayout linearAddress, linearPurchaseOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        linearAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressItemAdapter.isOrder = false;
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });

        linearPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YourOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl(View view) {
        linearAddress = view.findViewById(R.id.linearAddress);
        linearPurchaseOrder = view.findViewById(R.id.linearPurchaseOrder);
    }
}