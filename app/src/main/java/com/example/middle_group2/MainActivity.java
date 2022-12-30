package com.example.middle_group2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.adapter.ViewPagerAdapter;
import com.example.implement.CartOrderImp;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // component
    private ViewPager tabPager;
    private BottomNavigationView bottomNavigationView;
    CartOrderImp cartOrderImp = new CartOrderImp();
    public static BadgeDrawable badgeDrawable;
    public static int countCart = 0;

    // data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setControl();
        initial();
    }

    private void initial() {
        initialNavigation();
    }

    private void initialNavigation() {
        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.action_cart);
        this.countCart = cartOrderImp.count(LoginActivity.idUserLogin);
        if (this.countCart == 0) {
            badgeDrawable.setVisible(false);
        } else {
            badgeDrawable.setVisible(true);
        }
        badgeDrawable.setNumber(countCart);
        badgeDrawable.setHorizontalOffset(16);
        badgeDrawable.setBackgroundColor(Color.parseColor("#FB7181"));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        tabPager.setAdapter(viewPagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_home:
                        tabPager.setCurrentItem(0);
                        break;
                    case R.id.action_voucher:
                        tabPager.setCurrentItem(1);
                        break;
                    case R.id.action_cart:
                        tabPager.setCurrentItem(2);
                        break;
                    case R.id.action_account:
                        tabPager.setCurrentItem(3);
                        break;
                    case R.id.action_sign_out:
                        LoginActivity.idUserLogin = "";
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        finishAffinity();
                        startActivity(intent);
                        bottomNavigationView.getMenu().findItem(R.id.action_sign_out).setCheckable(true);
                        break;
                    default: break;
                }
                return true;
            }
        });

        tabPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_voucher).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_cart).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_account).setCheckable(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setControl() {
        bottomNavigationView = findViewById(R.id.navigationView);
        tabPager = findViewById(R.id.tabPager);
    }
}