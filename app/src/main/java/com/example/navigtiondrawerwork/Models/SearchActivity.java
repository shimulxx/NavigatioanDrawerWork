package com.example.navigtiondrawerwork.Models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigtiondrawerwork.MainActivity;
import com.example.navigtiondrawerwork.R;

import com.example.navigtiondrawerwork.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements ShowAllCategoriesDialog.SelectCategory{
    private static final String TAG = "SearchActivity";

    private EditText searchBar;
    private ImageView btnSearch;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private TextView textFirst, textSecond, textThird, textSeeAllCategories;
    private GroceryItemAdapter groceryItemAdapter;

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

        utils = new Utils(this);
        groceryItemAdapter = new GroceryItemAdapter(this);
        recyclerView.setAdapter(groceryItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initBottomNavigation();
        initThreeTextViews();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateSearch();
            }
        });

        textSeeAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAllCategoriesDialog showAllCategoriesDialog = new ShowAllCategoriesDialog();
                showAllCategoriesDialog.show(getSupportFragmentManager(), "All Dialog");
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<GroceryItem> groceryItems = utils.searchForItems(String.valueOf(s));
                groceryItemAdapter.setGroceryItems(groceryItems);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void initThreeTextViews(){
        Log.d(TAG, "initThreeTextViews: started");
        final ArrayList<String> categories = utils.getThreecategories();
        switch (categories.size()){
            case 1:
                textFirst.setText(categories.get(0));
                textSecond.setVisibility(View.GONE);
                textThird.setVisibility(View.GONE);
                break;
            case 2:
                textFirst.setText(categories.get(0));
                textSecond.setText(categories.get(1));
                textThird.setVisibility(View.GONE);
                break;
            case 3:
                textFirst.setText(categories.get(0));
                textSecond.setText(categories.get(1));
                textThird.setText(categories.get(2));
                break;
            default:
                break;
        }
        textFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ShowItemByCategoryActivity.class);
                intent.putExtra("category", textFirst.getText().toString());
                startActivity(intent);
            }
        });
        textSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ShowItemByCategoryActivity.class);
                intent.putExtra("category", textSecond.getText().toString());
                startActivity(intent);
            }
        });
        textThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ShowItemByCategoryActivity.class);
                intent.putExtra("category", textThird.getText().toString());
                startActivity(intent);
            }
        });
    }
    private void initiateSearch(){
        Log.d(TAG, "initiateSearch: started");
        String text = searchBar.getText().toString();
        ArrayList<GroceryItem> grocerySearchedItems = utils.searchForItems(text);
        for(GroceryItem groceryItem : grocerySearchedItems){
            utils.increaseUserPoint(groceryItem, 3);
        }
        groceryItemAdapter.setGroceryItems(grocerySearchedItems);
    }
    private void initViews(){
        searchBar = (EditText)findViewById(R.id.editTextSearchBar);
        btnSearch = (ImageView)findViewById(R.id.btnSearch);

        recyclerView = (RecyclerView)findViewById(R.id.searchRecview);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation_viewSearch);

        textFirst = (TextView)findViewById(R.id.firstCat);
        textSecond = (TextView)findViewById(R.id.secondCat);
        textThird = (TextView)findViewById(R.id.thirdCat);

        textSeeAllCategories = (TextView)findViewById(R.id.btnAllcategoriesSearch);
    }
    private void initBottomNavigation() {
        Log.d(TAG, "initBottomNavigation: started");
        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cartBottomNavigation:
                        Intent cartIntent = new Intent(SearchActivity.this, CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cartIntent);
                        break;
                    case R.id.search:
                        //TODO:: FIX this
                        break;
                    case R.id.home:
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onSelectCategoryResult(String category) {
        Log.d(TAG, "onSelectCategoryResult: " + category);
        Intent intent = new Intent(this, ShowItemByCategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
