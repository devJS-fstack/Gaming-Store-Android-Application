package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.adapter.YourOrderAdapter;
import com.example.implement.PurchaseOrderDetailImp;
import com.example.implement.PurchaseOrderImp;

import java.util.ArrayList;

import entities.PurchaseOrder;
import entities.PurchaseOrderDetail;

public class YourOrderActivity extends AppCompatActivity {

    // component
    RecyclerView rvListOrder;

    // adapter
    YourOrderAdapter yourOrderAdapter;

    // imp
    PurchaseOrderImp purchaseOrderImp = new PurchaseOrderImp();
    PurchaseOrderDetailImp purchaseOrderDetailImp = new PurchaseOrderDetailImp();

    // data
    ArrayList<PurchaseOrder> listPurchaseOrder = new ArrayList<>();
    private int[] listQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_order);
        setControl();
        initialData();
        setEvent();
    }

    private void initialData() {
        String idUser = LoginActivity.idUserLogin;
        listPurchaseOrder = purchaseOrderImp.findAll("WHERE id_user = '" + idUser  + "'");
        generateQuantity(listPurchaseOrder);
        yourOrderAdapter = new YourOrderAdapter(this, listPurchaseOrder, listQuantity);
        rvListOrder.setAdapter(yourOrderAdapter);
        rvListOrder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void generateQuantity(ArrayList<PurchaseOrder> purchaseOrders) {
        listQuantity = new int[purchaseOrders.size()];
        for (int i = 0; i < purchaseOrders.size(); i++) {
            String poNum = purchaseOrders.get(i).getPurchaseOrderNumber();
            int quantity = purchaseOrderDetailImp.count("WHERE po_number = '" + poNum + "'");
            listQuantity[i] = quantity;
        }
    }

    private void setEvent() {
    }

    private void setControl() {
        rvListOrder = findViewById(R.id.rvListOrder);
    }
}