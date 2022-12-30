package com.example.middle_group2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adapter.ReviewItemAdapter;
import com.example.implement.ReviewProductImp;

import java.util.ArrayList;
import java.util.HashMap;

public class MoreReview extends AppCompatActivity {

    private RecyclerView rvReview;
    private ArrayList<HashMap<String, Object>> listReviewProduct = new ArrayList<>();
    public static Button btnAll, btnOne, btnTwo, btnThree, btnFour, btnFive;
    Button lastBtn;

    private String idProduct = "";

    ReviewItemAdapter reviewItemAdapter;

    ReviewProductImp reviewProductImp = new ReviewProductImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_review);
        setControl();
        idProduct = (String) getIntent().getExtras().getSerializable("idProduct");
        initial();
        setEvent();
    }

    private void setEvent() {
        handleFilterReview(btnAll);
        handleFilterReview(btnOne);
        handleFilterReview(btnTwo);
        handleFilterReview(btnThree);
        handleFilterReview(btnFour);
        handleFilterReview(btnFive);
    }

    private void initial() {
        listReviewProduct = reviewProductImp.findManyType("JOIN users ON product_reviews.id_user = users.id_user and id_product = '" + idProduct + "'");
        reviewItemAdapter = new ReviewItemAdapter(this, listReviewProduct);
        rvReview.setAdapter(reviewItemAdapter);
        rvReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void handleFilterReview(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.setBackgroundTintList(MoreReview.this.getResources().getColorStateList(R.color.color_choose));
                btn.setTextColor(Color.parseColor("#FFFFFF"));
                if (lastBtn != btn) {
                    lastBtn.setBackgroundTintList(MoreReview.this.getResources().getColorStateList(R.color.color_unchoose));
                    lastBtn.setTextColor(Color.parseColor("#212121"));
                    lastBtn = btn;
                }

                String text = btn.getText().toString();
                if(text.contains("⭐ 1")) {
                    reviewItemAdapter.filter(1);
                } else if (text.contains("⭐ 2")) {
                    reviewItemAdapter.filter(2);
                } else if (text.contains("⭐ 3")) {
                    reviewItemAdapter.filter(3);
                } else if (text.contains("⭐ 4")) {
                    reviewItemAdapter.filter(4);
                } else if (text.contains("⭐ 5")) {
                    reviewItemAdapter.filter(5);
                } else {
                    reviewItemAdapter.filter(0);
                }
            }
        });
    }

    private void setControl() {
        rvReview = findViewById(R.id.rvReview);
        btnAll = findViewById(R.id.btnAll);
        lastBtn = btnAll;
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
    }
}