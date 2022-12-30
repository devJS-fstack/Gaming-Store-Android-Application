package com.example.middle_group2;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.example.adapter.ProductAdapter;
import com.example.implement.ProductImp;

import java.util.ArrayList;

import entities.Product;

public class SearchProductActivity extends AppCompatActivity {

    private String keySearch = "";
    private Boolean isEmpty = false;
    private int LIMIT = 6;
    private int OFFSET = 0;

    // adapter
    private ProductAdapter productAdapter;

    // component
    private RecyclerView rvListProduct;
    private NestedScrollView nestedScrollProduct;
    private ProgressBar pbProduct;
    private SearchView svProduct;

    // imp
    private ProductImp productImp = new ProductImp();

    // data
    private ArrayList<Product> listProduct;
    private ArrayList<Product> tempListProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_product);
        setControl();
        initial();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SearchProductActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setControl() {
        rvListProduct = findViewById(R.id.rvListProduct);
        nestedScrollProduct = findViewById(R.id.nestedScrollProduct);
        pbProduct = findViewById(R.id.pbProduct);
        svProduct = findViewById(R.id.svProduct);
    }

    private void initial() {
        initialData();
        initListProduct();
        handleScroll();
        handleSubmitSearch();
    }

    private void initListProduct() {
        if (listProduct != null) {
            productAdapter = new ProductAdapter(this);
            rvListProduct.setAdapter(productAdapter);
            productAdapter.setData(listProduct);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            rvListProduct.setLayoutManager(gridLayoutManager);
        }
    }

    private void initialData() {
        keySearch = (String) getIntent().getExtras().getSerializable("keySearch");
        svProduct.setQuery(keySearch, false);
        listProduct = productImp.findAll(convertQueryProduct(keySearch));
        if(listProduct.size() < LIMIT) {
            isEmpty = true;
        }
    }

    private void handleScroll() {
        pbProduct.getIndeterminateDrawable().setColorFilter(Color.parseColor("#40BFFF"), android.graphics.PorterDuff.Mode.MULTIPLY);
        nestedScrollProduct.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() && !isEmpty) {
                    pbProduct.setVisibility(View.VISIBLE);
                    addDataProduct();
                } else {
                    pbProduct.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void addDataProduct() {
        OFFSET += LIMIT;
        tempListProduct = productImp.findAll(convertQueryProduct(keySearch));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tempListProduct.size() > 0) {
                    listProduct.addAll(tempListProduct);
                    productAdapter.setData(listProduct);
                    productAdapter.notifyDataSetChanged();
                    if(tempListProduct.size() < LIMIT) {
                        System.out.println("Empty data");
                        isEmpty = true;
                    }
                } else {
                    System.out.println("Empty data");
                    isEmpty = true;
                }
                pbProduct.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    private String convertQueryProduct(String keySearch) {
        return "where name_product LIKE '%" + keySearch +"%' or id_category LIKE  '%" + keySearch + "%'" +
                "ORDER BY id_product desc OFFSET "
                + OFFSET + " ROWS FETCH NEXT "
                + LIMIT + " ROWS ONLY";
    }

    private void handleSubmitSearch() {
        svProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listProduct.clear();
                listProduct.addAll(productImp.findAll(convertQueryProduct(s)));
                productAdapter.notifyDataSetChanged();
                rvListProduct.startLayoutAnimation();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}