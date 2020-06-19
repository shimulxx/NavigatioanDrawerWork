package com.example.navigtiondrawerwork.Models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.navigtiondrawerwork.MainActivity;
import com.example.navigtiondrawerwork.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initBottomNavigationView();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_frameLayout_cart, new CartFirstFragment());
        fragmentTransaction.commit();
    }
    private void initBottomNavigationView(){
        Log.d(TAG, "initBottomNavigationView: started");
        bottomNavigationView.setSelectedItemId(R.id.cartBottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        Intent searchIntent = new Intent(CartActivity.this, SearchActivity.class);
                        searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(searchIntent);
                        break;
                    case R.id.home:
                        Intent homeIntent = new Intent(CartActivity.this, MainActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeIntent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    private void initView(){
        Log.d(TAG, "initViewa: started");
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_view_cart);
    }
}
