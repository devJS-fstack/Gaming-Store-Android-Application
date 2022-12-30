package com.example.middle_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.adapter.CategoryAdapter;
import com.example.adapter.ProductAdapter;
import com.example.adapter.SliderAdapter;
import com.example.implement.CategoryImp;
import com.example.implement.ProductImp;

import java.util.ArrayList;

import entities.Category;
import entities.Product;

public class HomeFragment extends Fragment {

    private View viewMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        this.viewMain = view;
        setControl(view);
        setEvent();
        return view;
    }

    private ProductImp productImp = new ProductImp();
    private RecyclerView listCategory;
    private RecyclerView listFlashSaleProduct;
    private ViewPager2 viewHomePage;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Integer> listImageSlider = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private Handler sliderHandler = new Handler();
    private SearchView svProduct;

    private int limitItem = 5;

    private CategoryImp categoryImp = new CategoryImp();

    private void setEvent() {
        initialViewHomePage();
        initialCategory();
        initialFlashSaleProduct();
        handleSubmitSearch();
    }

    private void initialViewHomePage() {
        listImageSlider.add(R.drawable.sale01);
        listImageSlider.add(R.drawable.sale02);
        SliderAdapter sliderAdapter = new SliderAdapter(listImageSlider, viewHomePage);
        viewHomePage.setAdapter(sliderAdapter);
    }

    private void initialCategory() {
        ArrayList<Category> categories = categoryImp.findAll();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        categoryAdapter = new CategoryAdapter(getActivity(), categories, linearLayoutManager);
        listCategory.setAdapter(categoryAdapter);
        listCategory.setLayoutManager(linearLayoutManager);
    }

    private void initialFlashSaleProduct() {
        productAdapter = new ProductAdapter(getActivity());
        listFlashSaleProduct.setAdapter(productAdapter);
        setFirstDataProduct();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        listFlashSaleProduct.setLayoutManager(gridLayoutManager);
        listFlashSaleProduct.setNestedScrollingEnabled(false);
    }

    private void setFirstDataProduct() {
        products = productImp.findAll("WHERE sale > 0 ORDER BY name_file_main desc OFFSET 0 ROWS FETCH NEXT " + limitItem + " ROWS ONLY");
        productAdapter.setData(products);
    }

    private void handleSubmitSearch() {
        svProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getActivity(), SearchProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keySearch", s);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void setControl(View view) {
        listCategory = view.findViewById(R.id.listCategory);
        listFlashSaleProduct = view.findViewById(R.id.listFlashSaleProduct);
        viewHomePage = view.findViewById(R.id.viewHomePage);
        svProduct = view.findViewById(R.id.svProduct);
    }
}