package com.example.middle_group2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adapter.ImageDetailAdapter;
import com.example.adapter.ProductAdapter;
import com.example.implement.ImageDetailCategoryImp;
import com.example.implement.ProductImp;

import java.util.ArrayList;

import entities.ImageDetailCategory;
import entities.ImageDetailProduct;
import entities.Product;
import me.relex.circleindicator.CircleIndicator;

public class ProductByCategory extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private RecyclerView rvListProduct;
    private NestedScrollView nestedScrollProduct;
    private TextView tvTitleCategory;
    private ProgressBar pbProduct;
    private int LIMIT = 6;
    private int OFFSET = 0;
    private Boolean isEmpty = false;
    private String idCategory;
    private ImageButton backProductByCategory;

    // adapter
    private ImageDetailAdapter imageDetailAdapter;
    private ProductAdapter productAdapter;

    // data
    private ArrayList<ImageDetailCategory> listImgByCategory;
    private ArrayList<Product> listProduct;
    private ArrayList<Product> tempListProduct;

    // imp
    private ImageDetailCategoryImp imageDetailCategoryImp = new ImageDetailCategoryImp();
    private ProductImp productImp = new ProductImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_by_category);
        setControl();
        initial();
    }

    private void initial() {
        initialData();
        initialBanner();
        initListProduct();
        handleScroll();
        handleOnBack();
    }

    private void initialData() {
        listImgByCategory = imageDetailCategoryImp.findAll("");
        idCategory = (String) getIntent().getExtras().getSerializable("idCategory");
        String nameCategory = (String) getIntent().getExtras().getSerializable("nameCategory");
        tvTitleCategory.setText(nameCategory);
        listProduct = productImp.findAll(convertQueryProduct());
        if(listProduct.size() < LIMIT) {
            isEmpty = true;
        }
    }

    private void initialBanner() {
        if(listImgByCategory != null ) {
            imageDetailAdapter = new ImageDetailAdapter(this, null , listImgByCategory);
            viewPager.setAdapter(imageDetailAdapter);
            circleIndicator.setViewPager(viewPager);
            imageDetailAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        }
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
        tempListProduct = productImp.findAll(convertQueryProduct());
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

    private String convertQueryProduct() {
        return "WHERE id_category = '" + idCategory + "' ORDER BY id_product desc OFFSET "
                + OFFSET + " ROWS FETCH NEXT "
                + LIMIT + " ROWS ONLY";
    }

    private void handleOnBack() {
        backProductByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        viewPager = findViewById(R.id.vpProductByCategory);
        circleIndicator = findViewById(R.id.indicatorProductByCategory);
        rvListProduct = findViewById(R.id.rvListProduct);
        nestedScrollProduct = findViewById(R.id.nestedScrollProduct);
        pbProduct = findViewById(R.id.pbProduct);
        tvTitleCategory = findViewById(R.id.tvTitleCategory);
        backProductByCategory = findViewById(R.id.backProductByCategory);
    }
}