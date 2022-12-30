package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.implement.CartOrderImp;
import com.example.implement.PurchaseOrderDetailImp;
import com.example.implement.PurchaseOrderImp;
import com.example.utils.Constants;
import com.example.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import entities.PurchaseOrder;
import entities.PurchaseOrderDetail;

public class VerifyOrderActivity extends AppCompatActivity {

    TextView tvEmailSent;
    EditText edtOTP;
    Button btnVerify;

    PurchaseOrderImp purchaseOrderImp = new PurchaseOrderImp();
    PurchaseOrderDetailImp purchaseOrderDetailImp = new PurchaseOrderDetailImp();
    CartOrderImp cartOrderImp = new CartOrderImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_order);
        // Component
        setControl();

        ArrayList<PurchaseOrderDetail> listPODetail = (ArrayList<PurchaseOrderDetail>)
                getIntent().getExtras().getSerializable("listPODetail");
        PurchaseOrder purchaseOrder = (PurchaseOrder)
                getIntent().getExtras().getSerializable("purchaseOrder");
        String[] listIdCart = (String[])
                getIntent().getExtras().getSerializable("listIdCart");
        String email = LoginActivity.emailUserLogin;
        String otp = (String) getIntent().getExtras().getSerializable("otp");

        // handle
        tvEmailSent.setText(email);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtOTP.getText().toString().equals(otp)) {
                    Boolean isSuccess = purchaseOrderImp.save(purchaseOrder)
                            && purchaseOrderDetailImp.insertMany(listPODetail)
                            && cartOrderImp.updateManyStatusCard(Constants.STATUS_ORDER_CART, listIdCart);
                    if(isSuccess) {
                        Toast.makeText(VerifyOrderActivity.this, "Order successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyOrderActivity.this, NotificationActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerifyOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOrderActivity.this, "Wrong OTP. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setControl() {
        tvEmailSent = findViewById(R.id.tvEmailSent);
        btnVerify = findViewById(R.id.btnVerify);
        edtOTP = findViewById(R.id.edtOTP);
    }
}