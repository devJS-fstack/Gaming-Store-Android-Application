package com.example.middle_group2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.CartAdapter;
import com.example.implement.CartOrderImp;

import java.util.ArrayList;
import java.util.HashMap;

import entities.Product;

public class YourCartFragment extends Fragment {

    // component
    RecyclerView rvListCart;
    TextView cartTotal, tempPrice, discountPrice, totalPrice;
    Button btnNextCart;

    // data
    private CartOrderImp cartOrderImp = new CartOrderImp();
    private ArrayList<HashMap<String, Object>> listCartOrder;

    // adapter
    CartAdapter cartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.your_cart_fragment, container, false);
        setControl(view);
        initial();
        return view;
    }

    private void setControl(View view) {
        rvListCart = view.findViewById(R.id.listCart);
        cartTotal = view.findViewById(R.id.cartTotal);
        tempPrice = view.findViewById(R.id.tempPrice);
        discountPrice = view.findViewById(R.id.discountPrice);
        totalPrice = view.findViewById(R.id.totalPrice);
        btnNextCart = view.findViewById(R.id.btnNextCart);

    }

    private void initial() {
        initialData();
        initialCart();
    }

    private void initialData() {
        listCartOrder = cartOrderImp.queryCart(LoginActivity.idUserLogin);
    }


    private void initialCart() {
        cartTotal.setText("Cart (" + listCartOrder.size() + ")");
        cartAdapter = new CartAdapter(getActivity(), listCartOrder, tempPrice,
                discountPrice, totalPrice,
                cartOrderImp, cartTotal,
                btnNextCart);
        if(listCartOrder.size() > 0) {
            MainActivity.badgeDrawable.setNumber(listCartOrder.size());
            MainActivity.badgeDrawable.setVisible(true);
        } else {
            MainActivity.badgeDrawable.setNumber(0);
            MainActivity.badgeDrawable.setVisible(false);
        }
        rvListCart.setAdapter(cartAdapter);
        rvListCart.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}