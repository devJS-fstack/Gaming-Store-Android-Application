package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.AddressItemAdapter;
import com.example.adapter.CartAdapter;
import com.example.adapter.ProductOrderDetailAdapter;
import com.example.implement.CartOrderImp;
import com.example.implement.PurchaseOrderDetailImp;
import com.example.implement.PurchaseOrderImp;
import com.example.utils.Constants;
import com.example.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import entities.Address;
import entities.Product;
import entities.PurchaseOrder;
import entities.PurchaseOrderDetail;

public class OrderDetailActivity extends AppCompatActivity {

    // component
    private RecyclerView rvProductOrder;
    private TextView tvFullName, tvPhone, tvStreet, tvCity,
            merchandiseTotal, shippingTotal, paymentTotal;

    private Button btnPlaceHolder;

    // adapter
    private ProductOrderDetailAdapter productOrderDetailAdapter;

    // imp
    PurchaseOrderImp purchaseOrderImp = new PurchaseOrderImp();
    PurchaseOrderDetailImp purchaseOrderDetailImp = new PurchaseOrderDetailImp();

    // variable
    private int payment = 0;
    private String idAddress;
    private ArrayList<HashMap<String, Object>> listProductOrder = new ArrayList<>();
    private ArrayList<PurchaseOrderDetail> listPODetail = new ArrayList<>();
    private String[] listIdCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        setControl();
        initial();
        handlePlaceHolder();
    }

    private void initial() {
        initialListProductOrder();
        initialShippingDetail();
        initialPaymentDetail();
    }

    private void initialListProductOrder() {
        listProductOrder.addAll(CartAdapter.listProductChecked);
        productOrderDetailAdapter = new ProductOrderDetailAdapter(this,  listProductOrder);
        rvProductOrder.setAdapter(productOrderDetailAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvProductOrder.setLayoutManager(linearLayoutManager);
    }

    private void initialShippingDetail() {
        Address address = AddressItemAdapter.addressSelected;
        tvFullName.setText(address.getFullName());
        tvCity.setText(address.getWard() + ", " + address.getDistrict() + ", " + address.getCity());
        tvPhone.setText(address.getPhone());
        tvStreet.setText(address.getStreet());
        idAddress = address.getIdAddress();
    }

    private void initialPaymentDetail() {
        merchandiseTotal.setText("$" + CartAdapter.totalPrice);
        shippingTotal.setText("$50");
        payment = CartAdapter.totalPrice + 50;
        paymentTotal.setText("$" + payment);
    }

    private void handlePlaceHolder() {
        btnPlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = Helper.generateRandomSixNumber();
                sendMail(LoginActivity.emailUserLogin, otp);
//                Boolean isSuccess = purchaseOrderImp.save(purchaseOrder)
//                        && purchaseOrderDetailImp.insertMany(listPODetail)
//                        && cartOrderImp.updateManyStatusCard(Constants.STATUS_ORDER_CART, listIdCart);
//                if(isSuccess) {
//                    Toast.makeText(OrderDetailActivity.this, "Order successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(OrderDetailActivity.this, NotificationActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(OrderDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void initialPODetail(String poNumber) {
        listIdCart = new String[listProductOrder.size()];
        for (int i = 0; i < listProductOrder.size(); i++) {
            HashMap<String, Object> map = listProductOrder.get(i);
            Product product = (Product) map.get("product");
            int quantity = Integer.parseInt((String) map.get("quantity"));
            String poNumberDetail = "JHBA" + generatePONumberDetail().toUpperCase(Locale.ROOT);
            int totalPrice = product.getCurrentPrice() * quantity;
            String idCart = (String) map.get("idCartOrder");
            listIdCart[i] = idCart;
            listPODetail.add(new PurchaseOrderDetail(poNumberDetail, quantity, totalPrice, product.getIdProduct(), poNumber));
        }
    }

    private String generatePONumber() {
        String poNumber;
        Boolean isExistID = false;
        do {
            poNumber = new Helper().generateRandomString();
            isExistID = purchaseOrderImp.isExistId(poNumber, "purchase_order_number");
        } while(isExistID);

        return poNumber;
    }

    private String generatePONumberDetail() {
        String poNumber;
        Boolean isExistID = false;
        do {
            poNumber = new Helper().generateRandomString();
            isExistID = purchaseOrderDetailImp.isExistId(poNumber, "po_num_detail");
        } while(isExistID);

        return poNumber;
    }

    private void sendMail(String email, String otp) {
        String sender = Constants.SENDER;
        String receiver = email;
        String password = Constants.PASSWORD_MAIL;
        System.out.println("send mail from " + sender + " to " + receiver);
        String host = Constants.HOST_MAIL;
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending OTP...");
        progressDialog.show();

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            mimeMessage.setSubject("Confirm OTP to verify your purchase order");
            mimeMessage.setText("Hello you 😍 This is your OTP: " + otp + " . Please use it to verify your purchase order");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                        progressDialog.dismiss();
                        System.out.println("send mail success");
                        doneSuccessMail(otp);
                    } catch (MessagingException e) {
                        System.out.println("error while sending email");
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        } catch (Exception e) {
            System.out.println("Error while sending email " + e);
        }
    }

    private void doneSuccessMail(String otp) {
        String poNumber = "BANQ" + generatePONumber().toUpperCase(Locale.ROOT);
        String idUser = LoginActivity.idUserLogin;
        PurchaseOrder purchaseOrder = new PurchaseOrder(poNumber, idUser, Constants.STATUS_ORDER, payment, "", idAddress);
        initialPODetail(poNumber);
        Intent intent = new Intent(OrderDetailActivity.this, VerifyOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("purchaseOrder", purchaseOrder);
        bundle.putSerializable("listPODetail", listPODetail);
        bundle.putSerializable("listIdCart", listIdCart);
        bundle.putSerializable("otp", otp);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setControl() {
        rvProductOrder = findViewById(R.id.rvProductDetail);
        tvFullName = findViewById(R.id.tvFullName);
        tvPhone = findViewById(R.id.tvPhone);
        tvStreet = findViewById(R.id.tvStreet);
        tvCity = findViewById(R.id.tvCity);
        merchandiseTotal = findViewById(R.id.merchandiseTotal);
        shippingTotal = findViewById(R.id.shippingTotal);
        paymentTotal = findViewById(R.id.totalPayment);
        btnPlaceHolder = findViewById(R.id.btnPlaceHolder);
    }
}